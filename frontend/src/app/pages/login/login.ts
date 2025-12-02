import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/authService';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  usuarioOCorreo: string = '';
  contrasena: string = '';
  errorMessage: string = '';
  mostrarContrasena: boolean = false;

  constructor(private authService: AuthService, private router: Router) { }

  toggleMostrarContrasena() {
    this.mostrarContrasena = !this.mostrarContrasena;
  }

  iniciarSesion() {
    this.errorMessage = '';

    const credentials = {
      email: this.usuarioOCorreo,
      password: this.contrasena
    };

    this.authService.login(credentials).subscribe({
      next: (response) => {
        console.log('Login exitoso', response);

        localStorage.setItem('accessToken', response.accessToken);
        localStorage.setItem('refreshToken', response.refreshToken);

        // Guardar información del usuario si es necesario
        localStorage.setItem('user', JSON.stringify(response.user));

        this.router.navigate(['/home']);
      },

      error: (error) => {
        console.error('Error de autenticacion', error);

        if (error.error && error.error.message) {
          this.errorMessage = error.error.message;
        } else {
          this.errorMessage = 'Error al iniciar sesión. Por favor intente más tarde.';
        }
      }
    });
  }
}
