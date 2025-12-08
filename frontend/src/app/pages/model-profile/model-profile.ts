import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ModelService, Model } from '../../services/models/model-service';
import { PostService, PostResponseDto } from '../../services/posts/post-service';

@Component({
  selector: 'app-model-profile',
  standalone: false,
  templateUrl: './model-profile.html',
  styleUrl: './model-profile.scss',
})
export class ModelProfile implements OnInit {
  modelName: string = '';
  modelData: Model | null = null;

  // --- Datos de la Modelo ---
  nombreModelo: string = '';
  biografia: string = '';
  profilePhotoUrl: string = '';
  videosCount: number = 0;
  isLoading: boolean = true;

  // --- Lógica de Paginación ---
  videosPorPagina: number = 6;
  paginaActual: number = 1;
  totalPaginas: number[] = [];

  // Array completo de todos los posts de la modelo
  todosLosPosts: PostResponseDto[] = [];

  // Array que se usará en el *ngFor (solo 6 posts)
  postsMostrados: PostResponseDto[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private modelService: ModelService,
    private postService: PostService
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.modelName = params.get('name') || '';
      console.log('Model name from route:', this.modelName);

      if (this.modelName) {
        this.cargarPerfilModelo(this.modelName);
      } else {
        console.error('No model name provided in route');
        this.isLoading = false;
      }
    });
  }

  cargarPerfilModelo(modelName: string) {
    console.log('Loading profile for model:', modelName);
    this.isLoading = true;
    let modelLoaded = false;
    let postsLoaded = false;

    const checkIfDone = () => {
      if (modelLoaded && postsLoaded) {
        this.isLoading = false;
        console.log('All data loaded successfully');
      }
    };

    // Fetch model details
    console.log('Fetching model details from:', `/api/models/name?name=${modelName}`);
    this.modelService.getModelByName(modelName).subscribe({
      next: (models: Model[]) => {
        console.log('Model response:', models);
        if (models && models.length > 0) {
          const model = models[0];
          this.modelData = model;
          this.nombreModelo = model.name;
          this.biografia = model.biography;
          this.profilePhotoUrl = model.profile?.s3Url || '';
          console.log('Model data loaded:', model);
        } else {
          console.warn('No models found for name:', modelName);
          this.nombreModelo = modelName;
          this.biografia = 'Información no disponible';
        }
        modelLoaded = true;
        checkIfDone();
      },
      error: (err) => {
        console.error('Error loading model details:', err);
        console.error('Error status:', err.status);
        console.error('Error message:', err.message);
        this.nombreModelo = modelName;
        this.biografia = 'Información no disponible';
        modelLoaded = true;
        checkIfDone();
      }
    });

    // Fetch posts by model name
    console.log('Fetching posts from:', `/api/posts/model-name?modelName=${modelName}`);
    this.postService.getPostsByModelName(modelName).subscribe({
      next: (posts: PostResponseDto[]) => {
        console.log('Posts response:', posts);
        // Handle null response from backend (when no posts found)
        this.todosLosPosts = posts || [];
        this.videosCount = this.todosLosPosts.length;
        this.calcularPaginacion();
        this.cambiarPagina(1);
        postsLoaded = true;
        checkIfDone();
      },
      error: (err) => {
        console.error('Error loading model posts:', err);
        console.error('Error status:', err.status);
        console.error('Error message:', err.message);
        this.todosLosPosts = [];
        this.videosCount = 0;
        postsLoaded = true;
        checkIfDone();
      }
    });
  }

  calcularPaginacion() {
    const numPaginas = Math.ceil(this.videosCount / this.videosPorPagina);
    this.totalPaginas = Array(numPaginas).fill(0).map((x, i) => i + 1);
  }

  cambiarPagina(nuevaPagina: number) {
    if (nuevaPagina < 1 || nuevaPagina > this.totalPaginas.length) {
      return;
    }

    this.paginaActual = nuevaPagina;

    const inicio = (this.paginaActual - 1) * this.videosPorPagina;
    const fin = inicio + this.videosPorPagina;

    this.postsMostrados = this.todosLosPosts.slice(inicio, fin);

    window.scrollTo({ top: 350, behavior: 'smooth' });
  }

  navegarAVideo(postId: number) {
    this.router.navigate(['/video', postId]);
  }

  formatDuration(seconds: number): string {
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`;
  }

  getFirstPrice(post: PostResponseDto): string {
    if (post.prices && post.prices.length > 0) {
      const price = post.prices[0];
      return `${price.currency} ${price.amount}`;
    }
    return 'N/A';
  }
}
