import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface SectionDto {
  name: string;
  description: string;
}

export interface SectionResponseDto {
  id: number;
  name: string;
  description: string;
}

@Injectable({
  providedIn: 'root',
})
export class SectionService {
  private apiUrl = `${environment.apiUrl}/sections`;

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('accessToken');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  // Public endpoint - no auth required
  getAllSections(): Observable<SectionResponseDto[]> {
    return this.http.get<SectionResponseDto[]>(`${this.apiUrl}`);
  }

  // Admin endpoints - require auth
  getSectionById(id: number): Observable<SectionResponseDto> {
    return this.http.get<SectionResponseDto>(`${this.apiUrl}/${id}`, {
      headers: this.getHeaders()
    });
  }

  createSection(section: SectionDto): Observable<SectionResponseDto> {
    return this.http.post<SectionResponseDto>(`${this.apiUrl}`, section, {
      headers: this.getHeaders()
    });
  }

  updateSection(id: number, section: SectionDto): Observable<SectionResponseDto> {
    return this.http.put<SectionResponseDto>(`${this.apiUrl}/${id}`, section, {
      headers: this.getHeaders()
    });
  }

  deleteSection(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, {
      headers: this.getHeaders()
    });
  }
}
