import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { marked } from 'marked';
import { PostService, PostResponseDto } from '../../services/posts/post-service';
import { ModelService, Model } from '../../services/models/model-service';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home implements OnInit {
  videosRecientes: any[] = []; // Will hold PostResponseDto objects with formatted duration
  modelosDestacados: Model[] = [];

  isLoggedIn: boolean = false; // Simular que el usuario está logeado
  isAdmin: boolean = false;    // Simular rol de administrador

  showAgeModal: boolean = false;
  accessDenied: boolean = false;

  constructor(
    private router: Router,
    private http: HttpClient,
    private postService: PostService,
    private modelService: ModelService
  ) { }

  ngOnInit(): void {
    // Verificar si el usuario ya confirmó su edad
    const ageVerified = localStorage.getItem('ageVerified');
    if (!ageVerified) {
      this.showAgeModal = true;
    }

    this.loadRecentPosts();
    this.loadSalientsModels();
  }

  loadRecentPosts() {
    this.postService.getRecentPosts().subscribe({
      next: (posts) => {
        this.videosRecientes = posts.map(post => ({
          ...post,
          precio: post.prices.find(p => p.codeCountry === 'CO')?.amount || 0, // Default to US price
          duracionFormatted: this.formatDuration(post.duration)
        }));
      },
      error: (err) => {
        console.error('Error loading recent posts:', err);
      }
    });
  }

  loadSalientsModels() {
    this.modelService.getSalientsModels().subscribe({
      next: (models) => {
        this.modelosDestacados = models;
      },
      error: (err) => {
        console.error('Error loading salients models:', err);
      }
    });
  }

  formatDuration(seconds: number): string {
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`;
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
}