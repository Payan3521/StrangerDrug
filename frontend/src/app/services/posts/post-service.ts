import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface SectionResponseDto {
  id: number;
  name: string;
  description: string;
}

export interface ModelDto {
  id: number;
  name: string;
  biography: string;
}

export interface PriceDto {
  codeCountry: string;
  country: string;
  amount: number;
  currency: string;
}

export interface PostResponseDto {
  id: number;
  title: string;
  description: string;
  videoKey: string;
  previewUrl: string;
  thumbnailUrl: string;
  section: SectionResponseDto;
  duration: number; // In seconds
  models: ModelDto[];
  prices: PriceDto[];
}

@Injectable({
  providedIn: 'root',
})
export class PostService {
  private apiUrl = `${environment.apiUrl}/posts`;

  constructor(private http: HttpClient) { }

  getRecentPosts(): Observable<PostResponseDto[]> {
    return this.http.get<PostResponseDto[]>(`${this.apiUrl}/recent`);
  }

  getAllPosts(): Observable<PostResponseDto[]> {
    return this.http.get<PostResponseDto[]>(`${this.apiUrl}`);
  }
}
