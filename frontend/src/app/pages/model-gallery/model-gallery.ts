import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

interface Model {
  id: number;
  nombre: string;
  videoCount: number;
  // Opcional: url de la imagen de perfil
  // imageUrl: string; 
}

@Component({
  selector: 'app-model-gallery',
  standalone: false,
  templateUrl: './model-gallery.html',
  styleUrl: './model-gallery.scss',
})
export class ModelGallery implements OnInit{
// Lista completa de modelos (Simulación)
  todosLosModelos: Model[] = [
    { id: 101, nombre: 'Sofía Castro', videoCount: 18 },
    { id: 102, nombre: 'Martha Sanchez', videoCount: 22 },
    { id: 103, nombre: 'Luisa García', videoCount: 15 },
    { id: 104, nombre: 'Juanita Robles', videoCount: 30 },
    { id: 105, nombre: 'Andrea Vasquez', videoCount: 12 },
    { id: 106, nombre: 'Stefa Montes', videoCount: 5 },
    { id: 107, nombre: 'Mariana Soto', videoCount: 25 },
    { id: 108, nombre: 'Vanesa Perez', videoCount: 19 },
    // Segunda Página
    { id: 109, nombre: 'Natalia Reyes', videoCount: 14 },
    { id: 110, nombre: 'Paola Turbay', videoCount: 8 },
    { id: 111, nombre: 'Camila Díaz', videoCount: 20 },
    { id: 112, nombre: 'Jessica Ríos', videoCount: 16 },
    { id: 113, nombre: 'Elena Mora', videoCount: 10 },
    { id: 114, nombre: 'Fabiola Gomez', videoCount: 28 },
    { id: 115, nombre: 'Diana Ruiz', videoCount: 11 },
    { id: 116, nombre: 'Laura Niño', videoCount: 35 },
    // Tercera Página
    { id: 117, nombre: 'Karla Luna', videoCount: 17 },
    { id: 118, nombre: 'Ximena Duque', videoCount: 6 },
  ];
  
  // Lista de modelos que se muestran en la vista (página actual)
  modelosMostrados: Model[] = [];

  // --- Lógica de Paginación ---
  // Estableceremos 8 modelos por página (2 filas de 4 tarjetas)
  modelosPorPagina: number = 8;
  paginaActual: number = 1;
  totalPaginas: number[] = [];
  
  // Búsqueda
  searchTerm: string = '';

  constructor(private router: Router) { }

  ngOnInit(): void {
    // Inicializar la paginación con la lista completa de modelos
    this.calcularPaginacion(this.todosLosModelos.length);
    this.cambiarPagina(1);
  }

  // Se ejecutará solo una vez al inicio
  cargarModelos() {
    console.log("Cargando la lista completa de modelos...");
  }
  
  calcularPaginacion(totalItems: number) {
    const numPaginas = Math.ceil(totalItems / this.modelosPorPagina);
    this.totalPaginas = Array(numPaginas).fill(0).map((x, i) => i + 1);
  }

  cambiarPagina(nuevaPagina: number) {
    if (nuevaPagina < 1 || nuevaPagina > this.totalPaginas.length) {
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
            modelo.nombre.toLowerCase().includes(this.searchTerm.toLowerCase())
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
  
  navegarAperfil(modelId: number) {
    this.router.navigate(['/model', modelId]);
  }
}
