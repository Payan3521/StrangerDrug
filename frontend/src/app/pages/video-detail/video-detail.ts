import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

// --- Tipo literal para los países permitidos ---
type Pais = 'Estados Unidos' | 'Colombia' | 'México' | 'España' | 'Otros';

@Component({
  selector: 'app-video-detail',
  standalone: false,
  templateUrl: './video-detail.html',
  styleUrl: './video-detail.scss',
})
export class VideoDetail implements OnInit {
  videoId: number = 0;
  tituloVideo: string = 'Gatica cachonda';
  descripcionVideo: string = 'Una descripción detallada sobre lo que el cliente está a punto de ver. Este contenido fue grabado en Miami y cuenta con la participación de las mejores modelos de la plataforma.';
  duracionVideo: string = '12:34';
  modeloVideo: string = 'Ana Ramírez';
  fechaPublicacion: string = 'Hace 2 horas';
  categoriaVideo: string = 'Lencería';


  isPurchased: boolean = false;
  isPreview: boolean = true;

  videoSourceUrl: string = 'assets/videos/preview_sample.mp4'; 
  
  // --- LÓGICA DE PRECIOS ---
  paises: Pais[] = ['Estados Unidos', 'Colombia', 'México', 'España', 'Otros'];

  paisSeleccionado: Pais = 'Estados Unidos';
  
  precios: Record<Pais, { moneda: string; precio: number }> = {
    'Estados Unidos': { moneda: 'USD', precio: 9.99 },
    'Colombia': { moneda: 'COP', precio: 40000 },
    'México': { moneda: 'MXN', precio: 199 },
    'España': { moneda: 'EUR', precio: 8.99 },
    'Otros': { moneda: 'USD', precio: 10.99 },
  };

  modelosRelacionados = [
      { id: 101, nombre: 'Ana Ramírez' },
      { id: 102, nombre: 'Luisa García' },
      { id: 103, nombre: 'Sofía Castro' }
  ];

  precioActual = this.precios['Estados Unidos'];

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.videoId = Number(params.get('id'));
      this.cargarDatosVideo(this.videoId);
      this.verificarEstadoCompra(this.videoId);
    });
  }

  cargarDatosVideo(id: number) {
    console.log(`Cargando información para el video ID: ${id}`);
  }

  verificarEstadoCompra(id: number) {
    if (this.isPurchased) {
      this.isPreview = false;
      this.videoSourceUrl = 'assets/videos/full_video.mp4';
    }
  }

  onPaisChange() {
    this.precioActual = this.precios[this.paisSeleccionado];
  }

  iniciarCompra() {
    console.log(
      `Iniciando proceso de pago por ${this.precioActual.moneda} ${this.precioActual.precio}`
    );
  }

  manejarFinPreview() {
    if (this.isPreview && !this.isPurchased) {
      console.log("Preview terminado. Requiere compra para continuar.");
    }
  }
}
