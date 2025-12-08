import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ModelService, Model } from '../../services/models/model-service';

@Component({
  selector: 'app-model-gallery',
  standalone: false,
  templateUrl: './model-gallery.html',
  styleUrl: './model-gallery.scss',
})
export class ModelGallery implements OnInit {
  // Lista completa de modelos
  todosLosModelos: Model[] = [];

  // Lista de modelos que se muestran en la vista (página actual)
  modelosMostrados: Model[] = [];

  // --- Lógica de Paginación ---
  // Estableceremos 8 modelos por página (2 filas de 4 tarjetas)
  modelosPorPagina: number = 8;
  paginaActual: number = 1;
  totalPaginas: number[] = [];

  // Búsqueda
  searchTerm: string = '';

  constructor(
    private router: Router,
    private modelService: ModelService
  ) { }

  ngOnInit(): void {
    this.cargarModelos();
  }

  // Se ejecutará solo una vez al inicio
  cargarModelos() {
    this.modelService.getAllModels().subscribe({
      next: (models) => {
        this.todosLosModelos = models;
        // Inicializar la paginación con la lista completa de modelos
        this.calcularPaginacion(this.todosLosModelos.length);
        this.cambiarPagina(1);
      },
      error: (err) => {
        console.error('Error loading models:', err);
      }
    });
  }

  calcularPaginacion(totalItems: number) {
    const numPaginas = Math.ceil(totalItems / this.modelosPorPagina);
    this.totalPaginas = Array(numPaginas).fill(0).map((x, i) => i + 1);
  }

  cambiarPagina(nuevaPagina: number) {
    if (nuevaPagina < 1 || (this.totalPaginas.length > 0 && nuevaPagina > this.totalPaginas.length)) {
      return;
    }

    this.paginaActual = nuevaPagina;
    this.aplicarFiltroYPaginacion();

    // Opcional: hacer scroll hacia arriba para ver la galería
    window.scrollTo({ top: 350, behavior: 'smooth' });
  }

  // Lógica para filtrar y luego paginar
  aplicarFiltroYPaginacion() {
    let modelosFiltrados = this.todosLosModelos;

    // 1. Filtrado (Si hay un término de búsqueda)
    if (this.searchTerm) {
      modelosFiltrados = this.todosLosModelos.filter(modelo =>
        modelo.name.toLowerCase().includes(this.searchTerm.toLowerCase())
      );
      // Recalcular paginación basada en la lista filtrada
      this.calcularPaginacion(modelosFiltrados.length);
      // Si la página actual es inválida después del filtro, volvemos a la 1
      if (this.paginaActual > this.totalPaginas.length && this.totalPaginas.length > 0) {
        this.paginaActual = 1;
      } else if (this.totalPaginas.length === 0) {
        this.modelosMostrados = [];
        return;
      }
    } else {
      // Recalcular paginación si el filtro está vacío (vuelve al total original)
      this.calcularPaginacion(this.todosLosModelos.length);
    }


    // 2. Paginación
    const inicio = (this.paginaActual - 1) * this.modelosPorPagina;
    const fin = inicio + this.modelosPorPagina;

    this.modelosMostrados = modelosFiltrados.slice(inicio, fin);
  }

  // Función vinculada al input de búsqueda
  onSearchChange(event: any) {
    this.searchTerm = event.target.value;
    // Siempre que se busca, volvemos a la página 1 y aplicamos filtro y paginación
    this.paginaActual = 1;
    this.aplicarFiltroYPaginacion();
  }

  navegarAperfil(modelName: string) {
    this.router.navigate(['/model', modelName]);
  }
}
