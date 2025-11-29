import { Component, ElementRef, OnInit, QueryList, ViewChildren } from '@angular/core';
import { Router } from '@angular/router';
// Definición de una interfaz simple para la estructura de la galería
interface Video {
  id: number;
  titulo: string;
  precio: number;
  duracion: string;
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

  categorias: VideoCategory[] = [
    {
      nombre: 'Góticas',
      videos: [
        { id: 10, titulo: 'Sesión Nocturna', precio: 12.99, duracion: '15:20' },
        { id: 11, titulo: 'Ritual de medianoche', precio: 8.99, duracion: '08:45' },
        { id: 12, titulo: 'Luz tenue', precio: 9.99, duracion: '12:34' },
        { id: 13, titulo: 'Vestido negro', precio: 15.99, duracion: '20:10' },
        { id: 14, titulo: 'Ojos ahumados', precio: 7.99, duracion: '05:30' },
        { id: 15, titulo: 'Sótanos prohibidos', precio: 11.99, duracion: '10:00' },
        { id: 16, titulo: 'Catedral Oscura', precio: 14.50, duracion: '19:00' },
      ]
    },
    {
      nombre: 'Rubias', 
      videos: [
        { id: 20, titulo: 'Playa en verano', precio: 10.50, duracion: '14:10' },
        { id: 21, titulo: 'Piscina privada', precio: 19.99, duracion: '25:00' },
        { id: 22, titulo: 'Mañana casual', precio: 14.00, duracion: '18:30' },
        { id: 23, titulo: 'Día de compras', precio: 9.00, duracion: '07:20' },
        { id: 24, titulo: 'Sol y Arena', precio: 16.99, duracion: '22:05' },
        { id: 25, titulo: 'Bikini blanco', precio: 12.00, duracion: '17:30' },
      ]
    },
    {
      nombre: 'Otros', 
      videos: [
        { id: 30, titulo: 'Escena de cama', precio: 13.50, duracion: '16:40' },
        { id: 31, titulo: 'Roleplay', precio: 8.50, duracion: '09:15' },
        { id: 32, titulo: 'POV', precio: 7.00, duracion: '06:00' },
        { id: 33, titulo: 'Fantasía', precio: 10.00, duracion: '13:00' },
      ]
    }
  ];

  constructor(private router: Router) { }

  ngOnInit(): void {
    this.cargarGaleria(); 
  }

  cargarGaleria() {
    console.log("Cargando todos los videos agrupados por categoría...");
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
