import { Component, OnInit } from '@angular/core';
import { SectionService, SectionDto, SectionResponseDto } from '../../services/sections/section-service';
import { ModelService, Model } from '../../services/models/model-service';
import { PostService, PostResponseDto } from '../../services/posts/post-service';
import { PurchaseService, PurchaseResponseDto } from '../../services/purchases/purchase-service';
import { AuthService } from '../../services/auth/authService';
import { Router } from '@angular/router';

@Component({
    selector: 'app-admin-panel',
    standalone: false,
    templateUrl: './admin-panel.html',
    styleUrl: './admin-panel.scss',
})
export class AdminPanel implements OnInit {
    activeTab: string = 'models';

    // Models
    models: Model[] = [];
    filteredModels: Model[] = [];
    modelSearch: string = '';
    selectedModel: Model | null = null;
    modelForm = {
        name: '',
        biography: '',
        file: null as File | null
    };

    // Sections
    sections: SectionResponseDto[] = [];
    filteredSections: SectionResponseDto[] = [];
    sectionSearch: string = '';
    selectedSection: SectionResponseDto | null = null;
    sectionForm: SectionDto = {
        name: '',
        description: ''
    };

    // Posts
    posts: PostResponseDto[] = [];
    filteredPosts: PostResponseDto[] = [];
    postSearch: string = '';
    selectedPost: PostResponseDto | null = null;
    postForm = {
        title: '',
        description: '',
        duration: 0,
        sectionName: '',
        selectedModels: [] as number[],
        prices: [] as { country: string; amount: number; currency: string; codeCountry: string }[],
        videoFile: null as File | null,
        previewFile: null as File | null,
        thumbnailFile: null as File | null
    };
    availableModels: Model[] = [];
    availableSections: SectionResponseDto[] = [];

    // Purchases
    purchases: PurchaseResponseDto[] = [];

    // UI State
    isLoading = false;
    showModal = false;
    modalMode: 'create' | 'edit' = 'create';
    errorMessage = '';
    successMessage = '';

    constructor(
        private sectionService: SectionService,
        private modelService: ModelService,
        private postService: PostService,
        private purchaseService: PurchaseService,
        private authService: AuthService,
        public router: Router
    ) { }

    ngOnInit(): void {
        // Verify admin access
        if (!this.authService.isAdmin()) {
            this.router.navigate(['/home']);
            return;
        }

        this.loadData();
    }

    loadData(): void {
        switch (this.activeTab) {
            case 'models':
                this.loadModels();
                break;
            case 'sections':
                this.loadSections();
                break;
            case 'posts':
                this.loadPosts();
                break;
            case 'sales':
                this.loadPurchases();
                break;
        }
    }

    switchTab(tab: string): void {
        this.activeTab = tab;
        this.closeModal();
        this.loadData();
    }

    // Models Management
    loadModels(): void {
        this.isLoading = true;
        this.modelService.getAllModels().subscribe({
            next: (models) => {
                this.models = models || [];
                this.filterModels();
                this.isLoading = false;
            },
            error: (err) => {
                this.showError('Error al cargar modelos');
                this.isLoading = false;
            }
        });
    }

    filterModels(): void {
        if (!this.modelSearch) {
            this.filteredModels = [...(this.models || [])];
        } else {
            const term = this.modelSearch.toLowerCase();
            this.filteredModels = (this.models || []).filter(m =>
                m.name.toLowerCase().includes(term) ||
                m.biography.toLowerCase().includes(term)
            );
        }
    }

    openModelModal(mode: 'create' | 'edit', model?: Model): void {
        this.modalMode = mode;
        if (mode === 'edit' && model) {
            this.selectedModel = model;
            this.modelForm.name = model.name;
            this.modelForm.biography = model.biography;
        } else {
            this.selectedModel = null;
            this.resetModelForm();
        }
        this.showModal = true;
    }

    saveModel(): void {
        if (!this.modelForm.name || !this.modelForm.biography) {
            this.showError('Nombre y biografía son requeridos');
            return;
        }

        if (this.modalMode === 'create' && !this.modelForm.file) {
            this.showError('La foto es requerida');
            return;
        }

        this.isLoading = true;

        const operation = this.modalMode === 'create'
            ? this.modelService.createModel(this.modelForm.name, this.modelForm.biography, this.modelForm.file!)
            : this.modelService.updateModel(this.selectedModel!.id, this.modelForm.name, this.modelForm.biography, this.modelForm.file!);

        operation.subscribe({
            next: () => {
                this.showSuccess(`Modelo ${this.modalMode === 'create' ? 'creado' : 'actualizado'} exitosamente`);
                this.closeModal();
                this.loadModels();
            },
            error: (err) => {
                this.showError(`Error al ${this.modalMode === 'create' ? 'crear' : 'actualizar'} modelo`);
                this.isLoading = false;
            }
        });
    }

    deleteModel(model: Model): void {
        if (!confirm(`¿Estás seguro de eliminar la modelo "${model.name}"?`)) {
            return;
        }

        this.isLoading = true;
        this.modelService.deleteModel(model.id).subscribe({
            next: () => {
                this.showSuccess('Modelo eliminado exitosamente');
                this.loadModels();
            },
            error: (err) => {
                this.showError('Error al eliminar modelo');
                this.isLoading = false;
            }
        });
    }

    resetModelForm(): void {
        this.modelForm = {
            name: '',
            biography: '',
            file: null
        };
    }

    onModelFileSelected(event: any): void {
        const file = event.target.files[0];
        if (file) {
            this.modelForm.file = file;
        }
    }

    // Sections Management
    loadSections(): void {
        this.isLoading = true;
        this.sectionService.getAllSections().subscribe({
            next: (sections) => {
                this.sections = sections || [];
                this.filterSections();
                this.isLoading = false;
            },
            error: (err) => {
                this.showError('Error al cargar secciones');
                this.isLoading = false;
            }
        });
    }

    filterSections(): void {
        if (!this.sectionSearch) {
            this.filteredSections = [...(this.sections || [])];
        } else {
            const term = this.sectionSearch.toLowerCase();
            this.filteredSections = (this.sections || []).filter(s =>
                s.name.toLowerCase().includes(term) ||
                s.description.toLowerCase().includes(term)
            );
        }
    }

    openSectionModal(mode: 'create' | 'edit', section?: SectionResponseDto): void {
        this.modalMode = mode;
        if (mode === 'edit' && section) {
            this.selectedSection = section;
            this.sectionForm = {
                name: section.name,
                description: section.description
            };
        } else {
            this.selectedSection = null;
            this.resetSectionForm();
        }
        this.showModal = true;
    }

    saveSection(): void {
        if (!this.sectionForm.name || !this.sectionForm.description) {
            this.showError('Nombre y descripción son requeridos');
            return;
        }

        this.isLoading = true;

        const operation = this.modalMode === 'create'
            ? this.sectionService.createSection(this.sectionForm)
            : this.sectionService.updateSection(this.selectedSection!.id, this.sectionForm);

        operation.subscribe({
            next: () => {
                this.showSuccess(`Sección ${this.modalMode === 'create' ? 'creada' : 'actualizada'} exitosamente`);
                this.closeModal();
                this.loadSections();
            },
            error: (err) => {
                this.showError(`Error al ${this.modalMode === 'create' ? 'crear' : 'actualizar'} sección`);
                this.isLoading = false;
            }
        });
    }

    deleteSection(section: SectionResponseDto): void {
        if (!confirm(`¿Estás seguro de eliminar la sección "${section.name}"?`)) {
            return;
        }

        this.isLoading = true;
        this.sectionService.deleteSection(section.id).subscribe({
            next: () => {
                this.showSuccess('Sección eliminada exitosamente');
                this.loadSections();
            },
            error: (err) => {
                this.showError('Error al eliminar sección');
                this.isLoading = false;
            }
        });
    }

    resetSectionForm(): void {
        this.sectionForm = {
            name: '',
            description: ''
        };
    }

    // Posts Management
    loadPosts(): void {
        this.isLoading = true;
        this.postService.getAllPosts().subscribe({
            next: (posts) => {
                this.posts = posts || [];
                this.filterPosts();
                this.isLoading = false;
            },
            error: (err) => {
                this.showError('Error al cargar publicaciones');
                this.isLoading = false;
            }
        });
    }

    filterPosts(): void {
        if (!this.postSearch) {
            this.filteredPosts = [...(this.posts || [])];
        } else {
            const term = this.postSearch.toLowerCase();
            this.filteredPosts = (this.posts || []).filter(p =>
                p.title.toLowerCase().includes(term) ||
                p.description.toLowerCase().includes(term) ||
                (p.section && p.section.name.toLowerCase().includes(term))
            );
        }
    }

    deletePost(post: PostResponseDto): void {
        if (!confirm(`¿Estás seguro de eliminar la publicación "${post.title}"?`)) {
            return;
        }

        this.isLoading = true;
        this.postService.deletePost(post.id).subscribe({
            next: () => {
                this.showSuccess('Publicación eliminada exitosamente');
                this.loadPosts();
            },
            error: (err) => {
                this.showError('Error al eliminar publicación');
                this.isLoading = false;
            }
        });
    }

    openPostModal(mode: 'create' | 'edit', post?: PostResponseDto): void {
        this.modalMode = mode;

        // Load models and sections for selection
        this.loadModelsForSelection();
        this.loadSectionsForSelection();

        if (mode === 'edit' && post) {
            this.selectedPost = post;
            this.postForm = {
                title: post.title,
                description: post.description,
                duration: post.duration,
                sectionName: post.section?.name || '',
                selectedModels: post.models.map(m => m.id),
                prices: post.prices.map(p => ({
                    country: p.country,
                    amount: p.amount,
                    currency: p.currency,
                    codeCountry: p.codeCountry
                })),
                videoFile: null,
                previewFile: null,
                thumbnailFile: null
            };
        } else {
            this.selectedPost = null;
            this.resetPostForm();
        }
        this.showModal = true;
    }

    loadModelsForSelection(): void {
        this.modelService.getAllModels().subscribe({
            next: (models) => {
                this.availableModels = models;
            },
            error: (err) => {
                console.error('Error loading models for selection:', err);
            }
        });
    }

    loadSectionsForSelection(): void {
        this.sectionService.getAllSections().subscribe({
            next: (sections) => {
                this.availableSections = sections;
            },
            error: (err) => {
                console.error('Error loading sections for selection:', err);
            }
        });
    }

    savePost(): void {
        // Validation
        if (!this.postForm.title || !this.postForm.description) {
            this.showError('Título y descripción son requeridos');
            return;
        }

        if (this.postForm.duration <= 0) {
            this.showError('La duración debe ser mayor a 0');
            return;
        }

        if (this.modalMode === 'create') {
            if (!this.postForm.videoFile || !this.postForm.previewFile || !this.postForm.thumbnailFile) {
                this.showError('Video, preview y thumbnail son requeridos');
                return;
            }
        }

        if (this.postForm.prices.length === 0) {
            this.showError('Debes agregar al menos un precio');
            return;
        }

        this.isLoading = true;

        // Build FormData
        const formData = new FormData();
        formData.append('title', this.postForm.title);
        formData.append('description', this.postForm.description);
        formData.append('duration', this.postForm.duration.toString());

        // Only add sectionName if it's not empty
        if (this.postForm.sectionName && this.postForm.sectionName.trim() !== '') {
            formData.append('sectionName', this.postForm.sectionName);
        }

        // Models as JSON array
        formData.append('models', JSON.stringify(this.postForm.selectedModels));

        // Prices as JSON array
        formData.append('prices', JSON.stringify(this.postForm.prices));

        // Files
        if (this.postForm.videoFile) {
            formData.append('video', this.postForm.videoFile);
        }
        if (this.postForm.previewFile) {
            formData.append('preview', this.postForm.previewFile);
        }
        if (this.postForm.thumbnailFile) {
            formData.append('thumbnail', this.postForm.thumbnailFile);
        }

        const operation = this.modalMode === 'create'
            ? this.postService.createPost(formData)
            : this.postService.updatePost(this.selectedPost!.id, formData);

        operation.subscribe({
            next: () => {
                this.showSuccess(`Publicación ${this.modalMode === 'create' ? 'creada' : 'actualizada'} exitosamente`);
                this.closeModal();
                this.loadPosts();
            },
            error: (err) => {
                this.showError(`Error al ${this.modalMode === 'create' ? 'crear' : 'actualizar'} publicación`);
                this.isLoading = false;
            }
        });
    }

    resetPostForm(): void {
        this.postForm = {
            title: '',
            description: '',
            duration: 0,
            sectionName: '',
            selectedModels: [],
            prices: [],
            videoFile: null,
            previewFile: null,
            thumbnailFile: null
        };
    }

    onPostVideoSelected(event: any): void {
        const file = event.target.files[0];
        if (file) {
            this.postForm.videoFile = file;
        }
    }

    onPostPreviewSelected(event: any): void {
        const file = event.target.files[0];
        if (file) {
            this.postForm.previewFile = file;
        }
    }

    onPostThumbnailSelected(event: any): void {
        const file = event.target.files[0];
        if (file) {
            this.postForm.thumbnailFile = file;
        }
    }

    toggleModelSelection(modelId: number): void {
        const index = this.postForm.selectedModels.indexOf(modelId);
        if (index > -1) {
            this.postForm.selectedModels.splice(index, 1);
        } else {
            this.postForm.selectedModels.push(modelId);
        }
    }

    isModelSelected(modelId: number): boolean {
        return this.postForm.selectedModels.includes(modelId);
    }

    addPrice(): void {
        this.postForm.prices.push({
            country: '',
            amount: 0,
            currency: 'COP',
            codeCountry: 'CO'
        });
    }

    removePrice(index: number): void {
        this.postForm.prices.splice(index, 1);
    }

    // Purchases Management
    loadPurchases(): void {
        this.isLoading = true;
        this.purchaseService.getAllPurchases().subscribe({
            next: (purchases) => {
                this.purchases = purchases || [];
                this.isLoading = false;
            },
            error: (err) => {
                console.error('Error loading purchases:', err);
                this.showError('Error al cargar ventas');
                this.isLoading = false;
            }
        });
    }

    deletePurchase(purchase: PurchaseResponseDto): void {
        if (!confirm(`¿Estás seguro de eliminar esta compra?`)) {
            return;
        }

        this.isLoading = true;
        this.purchaseService.softDeleteAdmin(purchase.id).subscribe({
            next: () => {
                this.showSuccess('Compra eliminada exitosamente');
                this.loadPurchases();
            },
            error: (err) => {
                this.showError('Error al eliminar compra');
                this.isLoading = false;
            }
        });
    }

    deleteAllPurchases(): void {
        if (!confirm('¿Estás seguro de eliminar TODAS las compras? Esta acción no se puede deshacer.')) {
            return;
        }

        this.isLoading = true;
        this.purchaseService.softDeleteAllAdmin().subscribe({
            next: () => {
                this.showSuccess('Todas las compras eliminadas exitosamente');
                this.loadPurchases();
            },
            error: (err) => {
                this.showError('Error al eliminar compras');
                this.isLoading = false;
            }
        });
    }

    // UI Helpers
    closeModal(): void {
        this.showModal = false;
        this.selectedModel = null;
        this.selectedSection = null;
        this.selectedPost = null;
        this.resetModelForm();
        this.resetSectionForm();
    }

    showError(message: string): void {
        this.errorMessage = message;
        setTimeout(() => this.errorMessage = '', 5000);
    }

    showSuccess(message: string): void {
        this.successMessage = message;
        setTimeout(() => this.successMessage = '', 5000);
    }

    formatDate(dateString: string): string {
        const date = new Date(dateString);
        return date.toLocaleDateString('es-ES');
    }

    formatDuration(seconds: number): string {
        const minutes = Math.floor(seconds / 60);
        const remainingSeconds = seconds % 60;
        return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`;
    }
}
