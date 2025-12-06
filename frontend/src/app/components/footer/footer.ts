import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { marked } from 'marked';

@Component({
    selector: 'app-footer',
    standalone: true,
    imports: [CommonModule, FontAwesomeModule],
    templateUrl: './footer.html',
    styleUrl: './footer.scss'
})
export class FooterComponent {
    showModal: boolean = false;
    modalTitle: string = '';
    modalContent: string = '';

    constructor(private http: HttpClient) { }

    openLegalModal(type: string) {
        let fileName = '';
        switch (type) {
            case 'privacy':
                this.modalTitle = 'Privacy Policy';
                fileName = 'privacy_policy.md';
                break;
            case 'terms':
                this.modalTitle = 'Terms and Conditions';
                fileName = 'terms_and_conditions.md';
                break;
            case 'support':
                this.modalTitle = 'Support';
                fileName = 'support.md';
                break;
            case 'about':
                this.modalTitle = 'About Us';
                fileName = 'about_us.md';
                break;
        }

        if (fileName) {
            this.showModal = true;
            this.http.get(`assets/${fileName}`, { responseType: 'text' })
                .subscribe({
                    next: (data) => {
                        this.modalContent = marked.parse(data) as string;
                    },
                    error: (err) => {
                        console.error('Error loading markdown:', err);
                        this.modalContent = '<p>Error loading content.</p>';
                    }
                });
        }
    }

    closeModal() {
        this.showModal = false;
        this.modalContent = '';
    }
}
