import { Component, ElementRef, OnInit, QueryList, ViewChildren } from '@angular/core';
import { Router } from '@angular/router';
import { PostService, PostResponseDto } from '../../services/posts/post-service';

interface Video {
  id: number;
  titulo: string;
  precio: number;
  duracion: string;
  thumbnailUrl: string;
}

interface VideoCategory {
  nombre: string;
  videos: Video[];
}

@Component({
  selector: 'app-video-gallery',
  standalone: false,
  templateUrl: './video-gallery.html',
  styleUrl: './video-gallery.scss',
})
export class VideoGallery implements OnInit {
  // Obtiene las referencias a todos los carruseles (div.carousel-wrapper)
  @ViewChildren('carouselWrapper') carouselWrappers!: QueryList<ElementRef>;

  // Ancho de desplazamiento (ej: 3 tarjetas + gap)
  scrollDistance: number = 980;

  categorias: VideoCategory[] = [];

  constructor(
    private router: Router,
    private postService: PostService
  ) { }

  ngOnInit(): void {
    this.cargarGaleria();
  }

  cargarGaleria() {
    this.postService.getAllPosts().subscribe({
      next: (posts) => {
        const grouped = new Map<string, Video[]>();

        posts.forEach(post => {
          const sectionName = post.section ? post.section.name : 'Otros';
          const video: Video = {
            id: post.id,
            titulo: post.title,
            precio: post.prices.find(p => p.codeCountry === 'CO')?.amount || 0,
            duracion: this.formatDuration(post.duration),
            thumbnailUrl: post.thumbnailUrl
          };

          if (!grouped.has(sectionName)) {
            grouped.set(sectionName, []);
          }
          grouped.get(sectionName)?.push(video);
        });

        this.categorias = Array.from(grouped.entries()).map(([nombre, videos]) => ({
          nombre,
          videos
        }));
      },
      error: (err) => {
        console.error('Error loading video gallery:', err);
      }
    });
  }

  formatDuration(seconds: number): string {
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`;
  }

  navegarAVideo(videoId: number) {
    this.router.navigate(['/video', videoId]);
  }

  /**
   * Mueve el carrusel de una categoría específica.
   * @param categoryIndex El índice de la categoría (0, 1, 2...).
   * @param direction -1 para izquierda (Anterior), 1 para derecha (Siguiente).
   */
  scrollCarousel(categoryIndex: number, direction: number) {
    // Usamos setTimeout para asegurar que carouselWrappers ya se haya llenado
    setTimeout(() => {
      const carouselElement = this.carouselWrappers.toArray()[categoryIndex]?.nativeElement;

      if (carouselElement) {
        const scrollAmount = direction * this.scrollDistance;
        carouselElement.scrollBy({ left: scrollAmount, behavior: 'smooth' });
      }
    });
  }
}
