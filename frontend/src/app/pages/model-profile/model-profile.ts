import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-model-profile',
  standalone: false,
  templateUrl: './model-profile.html',
  styleUrl: './model-profile.scss',
})
export class ModelProfile implements OnInit {
  modelId: number = 0;

  // --- Datos de la Modelo ---
  nombreModelo: string = 'Sofía Castro';
  videosCount: number = 18; // 18 videos en total (se actualiza al cargar)

  // --- Lógica de Paginación ---
  videosPorPagina: number = 6;
  paginaActual: number = 1;
  totalPaginas: number[] = [];

  // Array completo de todos los videos de la modelo (SIMULACIÓN)
  todosLosVideos = [
    { id: 10, titulo: 'Sesión Tropical', precio: 12.99, duracion: '15:20' },
    { id: 11, titulo: 'Detrás de Escena', precio: 8.99, duracion: '08:45' },
    { id: 12, titulo: 'Lencería Roja', precio: 9.99, duracion: '12:34' },
    { id: 13, titulo: 'Gimnasio Caliente', precio: 15.99, duracion: '20:10' },
    { id: 14, titulo: 'Baño de Espuma', precio: 7.99, duracion: '05:30' },
    { id: 15, titulo: 'Solo para Fans', precio: 11.99, duracion: '10:00' },
    { id: 16, titulo: 'Mañana en la Playa', precio: 10.50, duracion: '14:10' },
    { id: 17, titulo: 'Cena Privada', precio: 19.99, duracion: '25:00' },
    { id: 18, titulo: 'Fantasía Policial', precio: 14.00, duracion: '18:30' },
    { id: 19, titulo: 'Ropa de Látex', precio: 9.00, duracion: '07:20' },
    { id: 20, titulo: 'Amanecer en Bali', precio: 16.99, duracion: '22:05' },
    { id: 21, titulo: 'Yoga Nocturno', precio: 11.00, duracion: '11:55' },
    { id: 22, titulo: 'Fiesta de Espuma', precio: 13.50, duracion: '16:40' },
    { id: 23, titulo: 'Noche de Club', precio: 8.50, duracion: '09:15' },
    { id: 24, titulo: 'Paseo en Coche', precio: 7.00, duracion: '06:00' },
  ];

  // Array que se usará en el *ngFor (solo 6 videos)
  videosMostrados: any[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.modelId = Number(params.get('id'));
      this.cargarPerfilModelo(this.modelId);
      this.videosCount = this.todosLosVideos.length; // Actualizar el conteo
      this.calcularPaginacion(); // Calcular el número de botones
      this.cambiarPagina(1); // Cargar la primera página
    });
  }

  cargarPerfilModelo(id: number) {
    console.log(`Cargando perfil y videos de la modelo ID: ${id}`);
    // Aquí iría la llamada al servicio para obtener todos los videos (this.todosLosVideos)
  }

  calcularPaginacion() {
    // Calcula cuántas páginas hay en total
    const numPaginas = Math.ceil(this.videosCount / this.videosPorPagina);
    // Crea un array [1, 2, 3, ...] para usar en el *ngFor
    this.totalPaginas = Array(numPaginas).fill(0).map((x, i) => i + 1);
  }

  cambiarPagina(nuevaPagina: number) {
    if (nuevaPagina < 1 || nuevaPagina > this.totalPaginas.length) {
      return; // Prevenir errores
    }

    this.paginaActual = nuevaPagina;

    // Calcula los índices de inicio y fin para el slice
    const inicio = (this.paginaActual - 1) * this.videosPorPagina;
    const fin = inicio + this.videosPorPagina;

    // Filtra el array de videos para mostrar solo los de la página actual
    this.videosMostrados = this.todosLosVideos.slice(inicio, fin);

    // Opcional: hacer scroll hacia arriba para ver los nuevos videos
    window.scrollTo({ top: 350, behavior: 'smooth' });
  }

  navegarAVideo(videoId: number) {
    this.router.navigate(['/video', videoId]);
  }
}
