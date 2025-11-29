import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

interface PurchasedVideo {
  id: number;
  title: string;
  model: string;
  duration: string;
  purchaseDate: Date;
  price: number;
}

@Component({
  selector: 'app-library',
  standalone: false,
  templateUrl: './library.html',
  styleUrl: './library.scss',
})
export class Library implements OnInit {
  // Lista de videos que el cliente ha comprado (Simulación)
  purchasedVideos: PurchasedVideo[] = [
    { 
      id: 10, 
      title: 'Gatica Cachonda', 
      model: 'Ana Ramirez', 
      duration: '12:34 min', 
      purchaseDate: new Date(2025, 10, 20), 
      price: 9.99 
    },
    { 
      id: 11, 
      title: 'Blanquita Tierna', 
      model: 'Sofía Castro', 
      duration: '15:34 min', 
      purchaseDate: new Date(2025, 10, 25), 
      price: 15.99 
    },
    { 
      id: 12, 
      title: 'Sesión Tropical', 
      model: 'Sofía Castro', 
      duration: '08:45 min', 
      purchaseDate: new Date(2025, 11, 1), 
      price: 12.99 
    },
    { 
      id: 13, 
      title: 'Yoga Nocturno', 
      model: 'Natalia Reyes', 
      duration: '11:55 min', 
      purchaseDate: new Date(2025, 11, 2), 
      price: 11.00 
    },
  ];
  
 videosMostrados: PurchasedVideo[] = []; // Videos de la página actual
  searchTerm: string = '';
  
  // --- Lógica de Paginación ---
  videosPorPagina: number = 4;
  paginaActual: number = 1;
  totalPaginas: number[] = []; // Array para los botones [1, 2, 3...]

  constructor(private router: Router) { }

  ngOnInit(): void {
    // Aplicar la lógica de filtro y paginación al inicio
    this.aplicarFiltroYPaginacion();
  }

  // Se encarga de filtrar la lista Y luego paginar el resultado
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

  // Función vinculada al input de búsqueda
  onSearchChange(event: any) {
    this.searchTerm = event.target.value;
    // Siempre que se busca, volvemos a la página 1 y aplicamos filtro y paginación
    this.paginaActual = 1;
    this.aplicarFiltroYPaginacion();
  }

  // Función de navegación para ver el video (reutiliza la ruta de detalle de video)
  verVideo(videoId: number) {
    this.router.navigate(['/video', videoId]); 
  }

  removeVideoFromLibrary(videoId: number, videoTitle: string, event: Event): void {
    // Detiene la propagación para evitar que se ejecute (click)="verVideo(video.id)"
    event.stopPropagation();

    const confirmation = confirm(`¿Estás seguro de que deseas eliminar "${videoTitle}" de tu biblioteca? Si lo eliminas, deberás comprarlo de nuevo para acceder al video.`);

    if (confirmation) {
        // Lógica de eliminación (Simulación en Frontend)
        console.log(`Eliminando video ID: ${videoId}, Título: ${videoTitle}`);
        
        // 1. Actualiza la lista principal de videos comprados
        this.purchasedVideos = this.purchasedVideos.filter(v => v.id !== videoId);

        // 2. Vuelve a aplicar filtros y paginación para refrescar la vista
        this.aplicarFiltroYPaginacion();

        // Aquí iría la llamada a un servicio para notificar al backend la eliminación de la licencia.
    }
  }
}
