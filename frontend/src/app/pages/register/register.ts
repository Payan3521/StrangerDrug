import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { marked } from 'marked';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.html',
  styleUrl: './register.scss',
})
export class Register {
  nombre: string = '';
  correo: string = '';
  fechaNacimiento: string = '';
  contrasena: string = '';
  confirmarContrasena: string = '';
  aceptaTerminos: boolean = false;
  mostrarContrasena: boolean = false;
  mostrarConfirmarContrasena: boolean = false;

  mostrarModalTerminos: boolean = false;
  termsContent: string = '';

  errorMessage: string = '';
  successMessage: string = '';

  constructor(private router: Router, private http: HttpClient) {
    // Aquí podrías inyectar el AuthService si fueras a enviar los datos
  }

  toggleMostrarContrasena() {
    this.mostrarContrasena = !this.mostrarContrasena;
  }

  toggleMostrarConfirmarContrasena() {
    this.mostrarConfirmarContrasena = !this.mostrarConfirmarContrasena;
  }

  // NUEVOS MÉTODOS para el Modal
  abrirModalTerminos() {
    this.mostrarModalTerminos = true;
    if (!this.termsContent) {
      this.http.get('assets/terms_and_conditions.md', { responseType: 'text' })
        .subscribe({
          next: (data) => {
            this.termsContent = marked.parse(data) as string;
          },
          error: (err) => {
            console.error('Error loading terms:', err);
            this.termsContent = '<p>Error al cargar los términos y condiciones.</p>';
          }
        });
    }
  }

  cerrarModalTerminos() {
    this.mostrarModalTerminos = false;
  }

  registrarme() {
    this.errorMessage = '';
    this.successMessage = '';

    // Lógica de validación básica del Front-end
    if (this.contrasena !== this.confirmarContrasena) {
      this.errorMessage = 'Las contraseñas no coinciden.';
      return;
    }
    if (!this.aceptaTerminos) {
      this.errorMessage = 'Debes aceptar los términos y condiciones.';
      return;
    }

    // Aquí iría la lógica para llamar al AuthService.registro()
    console.log('Datos listos para enviar al backend:', {
      nombre: this.nombre,
      correo: this.correo,
      fechaNacimiento: this.fechaNacimiento,
      contrasena: this.contrasena,
      aceptaTerminos: this.aceptaTerminos
    });

    // Simulación de éxito (reemplazar con la llamada a AuthService)
    this.successMessage = 'Registro exitoso. ¡Ahora puedes iniciar sesión!';

    // Redirigir al login después de un breve retraso
    setTimeout(() => {
      this.router.navigate(['/login']);
    }, 2000);
  }
}