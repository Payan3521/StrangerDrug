import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
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
  createdAt: string;
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

  getPostById(id: number): Observable<PostResponseDto> {
    return this.http.get<PostResponseDto>(`${this.apiUrl}/${id}`);
  }

  getPostsByModelName(modelName: string): Observable<PostResponseDto[]> {
    return this.http.get<PostResponseDto[]>(`${this.apiUrl}/model-name`, {
      params: { modelName }
    });
  }

  // Admin methods
  createPost(postData: FormData): Observable<PostResponseDto> {
    const token = localStorage.getItem('accessToken');
    return this.http.post<PostResponseDto>(`${this.apiUrl}`, postData, {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${token}`
      })
    });
  }

  updatePost(id: number, postData: FormData): Observable<PostResponseDto> {
    const token = localStorage.getItem('accessToken');
    return this.http.put<PostResponseDto>(`${this.apiUrl}/${id}`, postData, {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${token}`
      })
    });
  }

  deletePost(id: number): Observable<void> {
    const token = localStorage.getItem('accessToken');
    return this.http.delete<void>(`${this.apiUrl}/${id}`, {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${token}`
      })
    });
  }
}
