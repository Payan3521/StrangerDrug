import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, catchError, Observable, switchMap, throwError } from 'rxjs';
import { AuthService } from '../services/auth/authService';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  private isRefreshing = false;
  private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);

  constructor(private authService: AuthService){}

  private excludedUrls: string[] = [
    'http://localhost:8080/api/auth/login',
    'http://localhost:8080/api/auth/refresh-token'
  ];

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>>{
      const accessToken = localStorage.getItem('accessToken');
      const isExcluded = this.excludedUrls.includes(request.url); // Verificar exclusión

      // 1. Manejo de peticiones excluidas (NO se añade token, NO se aplica manejo de 401)
      if (isExcluded) {
        return next.handle(request);
      }
      
      // 2. Manejo de peticiones protegidas sin token (simplemente fallan)
      if (!accessToken) {
          return next.handle(request); // Spring Security devolverá 401/403
      }
      
      // 3. Peticiones protegidas CON token (Aquí aplicamos el manejo de 401)
      const authRequest = this.addToken(request, accessToken);

      return next.handle(authRequest).pipe(
        catchError(error => {
            // El 401 solo se maneja si la petición llevaba el token
            if (error.status === 401 && !this.isRefreshing) {
              return this.handle401Error(request, next); // ¡Usar la request ORIGINAL!
            }
          return throwError(() => error);
        })
      );
  }

  private handle401Error(request: HttpRequest<any>, next: HttpHandler) {
        this.isRefreshing = true;
        this.refreshTokenSubject.next(null);
        
        const refreshToken = localStorage.getItem('refreshToken');

        if (refreshToken) {
            // 3. Llamar al servicio de refresco
            return this.authService.refreshToken(refreshToken).pipe(
                switchMap((response: any) => {
                    this.isRefreshing = false;
                    
                    // 4. Guardar los nuevos tokens
                    localStorage.setItem('accessToken', response.accessToken);
                    localStorage.setItem('refreshToken', response.refreshToken);
                    this.refreshTokenSubject.next(response.accessToken);

                    // 5. Re-enviar la petición original fallida con el nuevo token
                    return next.handle(this.addToken(request, response.accessToken));
                }),
                catchError((err) => {
                    // Si el refresco falla (ej: Refresh Token inválido/expirado)
                    this.isRefreshing = false;
                    this.authService.logout(); // ⬅️ Método a implementar
                    return throwError(() => err);
                })
            );
        }

        // Si no hay Refresh Token, el usuario debe iniciar sesión de nuevo
        this.isRefreshing = false;
        this.authService.logout();
        return throwError(() => 'Refresh Token no disponible o expirado');
    }

    private addToken(request: HttpRequest<any>, token: string) {
        return request.clone({
            headers: request.headers.set('Authorization', `Bearer ${token}`)
        });
    }
}