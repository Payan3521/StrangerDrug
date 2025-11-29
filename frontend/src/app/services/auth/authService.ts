import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/stranger-drug/api/auth';

  constructor(private http: HttpClient){}

  login(email: string, password:string): Observable<any>{
    const body = { email, password };
    return this.http.post(`${this.apiUrl}/login`, body); // ⬅️ Endpoint /login
  }

  refreshToken(oldRefreshToken: string): Observable<any>{
    const body = { refreshToken: oldRefreshToken };
    return this.http.post(`${this.apiUrl}/refresh-token`, body); // ⬅️ Endpoint /refresh-token
  }

  logout() {
    // 1. Limpia los tokens del almacenamiento
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    
    // 2. (Opcional) Navega al login
    // Necesitarías inyectar el Router aquí:
    // constructor(private http: HttpClient, private router: Router) {}
    // this.router.navigate(['/login']); 

    // Puedes agregar una llamada al backend si quieres invalidar el token en DB inmediatamente
    // const refreshToken = localStorage.getItem('refreshToken');
    // if (refreshToken) {
    //   this.http.post(`${this.baseUrl}/logout`, { refreshToken }).subscribe();
    // }
  }
}
