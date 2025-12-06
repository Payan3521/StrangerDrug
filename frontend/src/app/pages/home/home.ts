import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { marked } from 'marked';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home implements OnInit {
  videosRecientes = [
    { id: 1, titulo: 'Título del video #1', precio: 9.99, duracion: '12:34' },
    { id: 2, titulo: 'Título del video #2', precio: 9.99, duracion: '12:34' },
    { id: 3, titulo: 'Título del video #3', precio: 9.99, duracion: '12:34' },
  ];

  modelosDestacados = [
    { id: 1, nombre: 'Sofía Castro', videos: 5 },
    { id: 2, nombre: 'Martha Sanchez', videos: 10 },
    { id: 3, nombre: 'Luisa García', videos: 7 },
    { id: 4, nombre: 'Juanita Robles', videos: 11 },
  ];

  isLoggedIn: boolean = false; // Simular que el usuario está logeado
  isAdmin: boolean = false;    // Simular rol de administrador

  // showModal, modalTitle, modalContent removed

  showAgeModal: boolean = false;
  accessDenied: boolean = false;

  constructor(private router: Router, private http: HttpClient) { }

  ngOnInit(): void {
    // Verificar si el usuario ya confirmó su edad
    const ageVerified = localStorage.getItem('ageVerified');
    if (!ageVerified) {
      this.showAgeModal = true;
    }
  }

  confirmAge() {
    localStorage.setItem('ageVerified', 'true');
    this.showAgeModal = false;
  }

  denyAge() {
    this.showAgeModal = false;
    this.accessDenied = true;
  }

  iniciarSesion() {
    this.router.navigate(['/login']);
  }

  cerrarSesion() {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    this.isLoggedIn = false;
    this.isAdmin = false;
    this.router.navigate(['/home']);
  }

  verTodos(tipo: string) {
    console.log(`Navegando a la sección de todos los ${tipo}`);
    // Aquí iría el routerLink para navegar a la lista completa
  }

  // Modal logic moved to FooterComponent
}