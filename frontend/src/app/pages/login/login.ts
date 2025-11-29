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

    this.authService.login(this.usuarioOCorreo, this.contrasena).subscribe({
      next: (response) => {
        console.log('Login exitoso', response);

        localStorage.setItem('accessToken', response.accessToken);
        localStorage.setItem('refreshToken', response.refreshToken);

        this.router.navigate(['/home'])
      },

      error: (error) => {
        console.error('Error de autenticacion', error);

        if (error.status === 401) {
          this.errorMessage = 'Credenciales invalidas';
        } else {
          this.errorMessage = 'Error al iniciar sesión';
        }
      }
    })
  }
}
