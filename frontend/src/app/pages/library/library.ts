import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PurchaseService, PurchaseResponseDto } from '../../services/purchases/purchase-service';
import { PostService, PostResponseDto } from '../../services/posts/post-service';
import { AuthService } from '../../services/auth/authService';
import { forkJoin } from 'rxjs';

interface PurchasedVideo {
  purchaseId: number;
  postId: number;
  title: string;
  model: string;
  duration: string;
  purchaseDate: Date;
  price: number;
  thumbnailUrl: string;
  videoKey: string;
}

@Component({
  selector: 'app-library',
  standalone: false,
  templateUrl: './library.html',
  styleUrl: './library.scss',
})
export class Library implements OnInit {
  purchasedVideos: PurchasedVideo[] = [];
  videosMostrados: PurchasedVideo[] = [];
  searchTerm: string = '';
  isLoading: boolean = true;

  // --- Lógica de Paginación ---
  videosPorPagina: number = 4;
  paginaActual: number = 1;
  totalPaginas: number[] = [];

  constructor(
    private router: Router,
    private purchaseService: PurchaseService,
    private postService: PostService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.loadUserPurchases();
  }

  loadUserPurchases(): void {
    const userId = this.authService.getUserId();

    if (!userId) {
      console.error('User not logged in');
      this.isLoading = false;
      return;
    }

    this.purchaseService.getPurchasesByUserId(userId).subscribe({
      next: (purchases: PurchaseResponseDto[]) => {
        if (purchases && purchases.length > 0) {
          // Get all post IDs from purchases
          const postRequests = purchases.map(purchase => {
            // Extract postId from video.s3Key or use a mapping
            // For now, we'll need to fetch all posts and match by videoKey
            return this.postService.getAllPosts();
          });

          // Fetch all posts once
          this.postService.getAllPosts().subscribe({
            next: (posts: PostResponseDto[]) => {
              this.purchasedVideos = purchases
                .filter(purchase => purchase.statusPurchaseCliente) // Only show active purchases
                .map(purchase => {
                  // Find the corresponding post by matching videoKey
                  const post = posts.find(p => p.videoKey === purchase.video.s3Key);

                  return {
                    purchaseId: purchase.id,
                    postId: post?.id || 0,
                    title: post?.title || 'Video sin título',
                    model: post?.models && post.models.length > 0
                      ? post.models.map(m => m.name).join(', ')
                      : 'Modelo desconocido',
                    duration: post?.duration ? this.formatDuration(post.duration) : '00:00',
                    purchaseDate: new Date(purchase.createdAt),
                    price: purchase.payment.amount,
                    thumbnailUrl: post?.thumbnailUrl || '',
                    videoKey: purchase.video.s3Key
                  };
                });

              this.isLoading = false;
              this.aplicarFiltroYPaginacion();
            },
            error: (err) => {
              console.error('Error loading posts:', err);
              this.isLoading = false;
            }
          });
        } else {
          this.isLoading = false;
          this.aplicarFiltroYPaginacion();
        }
      },
      error: (err) => {
        console.error('Error loading purchases:', err);
        this.isLoading = false;
      }
    });
  }

  formatDuration(seconds: number): string {
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    return `${minutes}:${remainingSeconds.toString().padStart(2, '0')} min`;
  }

  aplicarFiltroYPaginacion() {
    let videosFiltrados = this.purchasedVideos;

    // 1. Filtrado
    if (this.searchTerm) {
      const term = this.searchTerm.toLowerCase();
      videosFiltrados = this.purchasedVideos.filter(video =>
        video.title.toLowerCase().includes(term) ||
        video.model.toLowerCase().includes(term)
      );
    }

    // 2. Cálculo de Paginación
    this.calcularPaginacion(videosFiltrados.length);

    // Si la página actual es inválida después del filtro, ajustamos
    if (this.paginaActual > this.totalPaginas.length && this.totalPaginas.length > 0) {
      this.paginaActual = 1;
    } else if (this.totalPaginas.length === 0) {
      this.videosMostrados = [];
      return;
    }

    // 3. Paginación
    const inicio = (this.paginaActual - 1) * this.videosPorPagina;
    const fin = inicio + this.videosPorPagina;

    this.videosMostrados = videosFiltrados.slice(inicio, fin);
  }

  calcularPaginacion(totalItems: number) {
    const numPaginas = Math.ceil(totalItems / this.videosPorPagina);
    this.totalPaginas = Array(numPaginas).fill(0).map((x, i) => i + 1);
  }

  cambiarPagina(nuevaPagina: number) {
    if (nuevaPagina < 1 || nuevaPagina > this.totalPaginas.length) {
      return;
    }

    this.paginaActual = nuevaPagina;
    this.aplicarFiltroYPaginacion();

    // Opcional: hacer scroll hacia el inicio de la cuadrícula
    window.scrollTo({ top: 350, behavior: 'smooth' });
  }

  onSearchChange(event: any) {
    this.searchTerm = event.target.value;
    this.paginaActual = 1;
    this.aplicarFiltroYPaginacion();
  }

  verVideo(postId: number) {
    if (postId) {
      this.router.navigate(['/video', postId]);
    }
  }

  removeVideoFromLibrary(purchaseId: number, videoTitle: string, event: Event): void {
    event.stopPropagation();

    const confirmation = confirm(`¿Estás seguro de que deseas eliminar "${videoTitle}" de tu biblioteca? Si lo eliminas, deberás comprarlo de nuevo para acceder al video.`);

    if (confirmation) {
      this.purchaseService.softDeletePurchase(purchaseId).subscribe({
        next: () => {
          // Remove from local array
          this.purchasedVideos = this.purchasedVideos.filter(v => v.purchaseId !== purchaseId);
          this.aplicarFiltroYPaginacion();
        },
        error: (err) => {
          console.error('Error deleting purchase:', err);
          alert('Error al eliminar la compra. Por favor, intenta de nuevo.');
        }
      });
    }
  }
}
