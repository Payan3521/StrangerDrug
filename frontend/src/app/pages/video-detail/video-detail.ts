import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PostService, PostResponseDto } from '../../services/posts/post-service';
import { VideoService } from '../../services/videos/video-service';
import { AuthService } from '../../services/auth/authService';
import { PurchaseService, PurchaseCreateDto } from '../../services/purchases/purchase-service';

// --- Tipo literal para los países permitidos ---
// type Pais = 'Estados Unidos' | 'Colombia' | 'México' | 'España' | 'Otros';

@Component({
  selector: 'app-video-detail',
  standalone: false,
  templateUrl: './video-detail.html',
  styleUrl: './video-detail.scss',
})
export class VideoDetail implements OnInit {
  postId: number = 0;
  videoKey: string = '';
  tituloVideo: string = 'Gatica cachonda';
  descripcionVideo: string = 'Una descripción detallada...';
  duracionVideo: string = '12:34';
  modeloVideo: string = 'Ana Ramírez';
  fechaPublicacion: string = 'Hace 2 horas';
  categoriaVideo: string = 'Lencería';


  isPurchased: boolean = false;
  isPreview: boolean = true;
  isPurchasing: boolean = false; // Flag to prevent multiple purchases

  videoSourceUrl: string = 'assets/videos/preview_sample.mp4';

  // --- LÓGICA DE PRECIOS ---
  paises: string[] = []; // Dynamic list of countries

  paisSeleccionado: string = '';

  precios: Record<string, { moneda: string; precio: number }> = {};

  modelosRelacionados = [
    { id: 101, nombre: 'Ana Ramírez' },
    { id: 102, nombre: 'Luisa García' },
    { id: 103, nombre: 'Sofía Castro' }
  ];

  precioActual: { moneda: string; precio: number } | undefined;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private postService: PostService,
    private videoService: VideoService,
    private authService: AuthService,
    private purchaseService: PurchaseService
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.postId = Number(params.get('id'));
      this.cargarDatosVideo(this.postId);
      this.verificarEstadoCompra(this.postId);
    });
  }

  cargarDatosVideo(id: number) {
    this.postService.getPostById(id).subscribe({
      next: (post: PostResponseDto) => {
        this.tituloVideo = post.title;
        this.descripcionVideo = post.description;
        this.duracionVideo = this.formatDuration(post.duration);
        this.categoriaVideo = post.section?.name || '';
        this.videoSourceUrl = post.previewUrl;
        this.fechaPublicacion = this.calculateTimeAgo(post.createdAt);
        this.videoKey = post.videoKey; // Save the videoKey from the post

        // Map models
        this.modelosRelacionados = post.models.map((m: any) => ({
          id: m.id,
          nombre: m.name
        }));

        // Reset prices and countries
        this.precios = {};
        this.paises = [];

        // Map prices
        if (post.prices && post.prices.length > 0) {
          post.prices.forEach((price: any) => {
            // Add country to the list if not already present
            if (!this.paises.includes(price.country)) {
              this.paises.push(price.country);
            }

            this.precios[price.country] = {
              moneda: price.currency,
              precio: price.amount
            };
          });

          // Set initial price based on default selection, or fallback to first available
          if (this.paises.length > 0) {
            // Try to find a default if possible, otherwise pick the first one
            const defaultCountry = 'Estados Unidos';
            if (this.paises.includes(defaultCountry)) {
              this.paisSeleccionado = defaultCountry;
            } else {
              this.paisSeleccionado = this.paises[0];
            }
            this.onPaisChange();
          } else {
            this.precioActual = undefined;
          }
        } else {
          this.precioActual = undefined;
        }
      },
      error: (err: any) => {
        console.error('Error loading video data:', err);
      }
    });
  }

  formatDuration(seconds: number): string {
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`;
  }

  calculateTimeAgo(createdAt: string): string {
    const now = new Date();
    const created = new Date(createdAt);
    const diffMs = now.getTime() - created.getTime();
    const diffHours = Math.floor(diffMs / (1000 * 60 * 60));
    const diffDays = Math.floor(diffHours / 24);

    if (diffDays > 0) {
      return `Hace ${diffDays} día${diffDays > 1 ? 's' : ''}`;
    } else if (diffHours > 0) {
      return `Hace ${diffHours} hora${diffHours > 1 ? 's' : ''}`;
    } else {
      return 'Hace menos de una hora';
    }
  }

  verificarEstadoCompra(videoId: number) {
    // TODO: Implement purchase verification logic
    // For now, we'll check when the user tries to play the full video
  }

  verModelo(modeloNombre: string) {
    console.log('Navegando al perfil del modelo:', modeloNombre);
    this.router.navigate(['/model', modeloNombre]);
  }

  onPaisChange() {
    if (this.precios[this.paisSeleccionado]) {
      this.precioActual = this.precios[this.paisSeleccionado];
    }
  }

  iniciarCompra() {
    // Validate user is logged in
    if (!this.authService.isLoggedIn()) {
      alert('Debes iniciar sesión para comprar este video.');
      return;
    }

    // Validate price is selected
    if (!this.precioActual) {
      alert('Por favor, selecciona un país para ver el precio.');
      return;
    }

    // Validate videoKey exists
    if (!this.videoKey) {
      alert('Error: No se pudo obtener la información del video.');
      return;
    }

    // Prevent multiple purchases
    if (this.isPurchasing) {
      return;
    }

    const userId = this.authService.getUserId();
    if (!userId) {
      alert('Error al obtener la información del usuario.');
      return;
    }

    // Confirm purchase
    const confirmMessage = `¿Confirmas la compra de "${this.tituloVideo}" por ${this.precioActual.moneda} ${this.precioActual.precio}?`;
    if (!confirm(confirmMessage)) {
      return;
    }

    this.isPurchasing = true;

    // Create purchase DTO with simulated payment
    const purchaseDto: PurchaseCreateDto = {
      buyerUserId: userId,
      videoKey: this.videoKey,
      paymentId: 1, // Simulated payment ID
      amount: this.precioActual.precio
    };

    console.log('Creando compra:', purchaseDto);

    this.purchaseService.createPurchase(purchaseDto).subscribe({
      next: (purchase) => {
        console.log('Compra exitosa:', purchase);
        this.isPurchasing = false;
        this.isPurchased = true;
        alert('¡Compra exitosa! Ahora puedes ver el video completo.');

        // Automatically play the full video after purchase
        this.playFullVideo();
      },
      error: (err) => {
        console.error('Error al crear la compra:', err);
        this.isPurchasing = false;

        // Handle specific error messages
        if (err.error && err.error.message) {
          alert(`Error: ${err.error.message}`);
        } else if (err.status === 400) {
          alert('Ya has comprado este video o hay un error en los datos.');
        } else {
          alert('Error al procesar la compra. Por favor, intenta de nuevo.');
        }
      }
    });
  }

  manejarFinPreview() {
    if (this.isPreview && !this.isPurchased) {
      console.log("Preview terminado. Requiere compra para continuar.");
    }
  }

  playFullVideo() {
    if (!this.authService.isLoggedIn()) {
      alert('Debes iniciar sesión para ver el video completo.');
      return;
    }

    const email = this.authService.getUserEmail();
    if (!email) {
      alert('Error al obtener la información del usuario.');
      return;
    }

    this.videoService.getPlayableUrl(this.videoKey, email).subscribe({
      next: (video: any) => {
        console.log('Video response from backend:', video); // Debug log
        if (video && video.s3Url) {
          console.log('Playing full video from URL:', video.s3Url); // Debug log
          this.videoSourceUrl = video.s3Url;
          this.isPreview = false;
          // this.isPurchased = true; // Uncomment if you want to persist purchased state in UI immediately

          // Allow Angular to update the DOM and [src] binding
          setTimeout(() => {
            const videoElement = document.querySelector('video');
            if (videoElement) {
              videoElement.load();
              videoElement.play().catch(err => console.error('Error playing video:', err));
            }
          }, 100);
        }
      },
      error: (err) => {
        console.error('Error getting playable URL:', err);
        alert('No tienes acceso a este video. Por favor, cómpralo primero.');
      }
    });
  }
}
