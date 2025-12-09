import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Photo {
  id: number;
  s3Bucket: string;
  s3Key: string;
  type: 'THUMBNAIL' | 'PROFILE';
  s3Url: string;
}

export interface Model {
  id: number;
  name: string;
  biography: string;
  profile: Photo;
}

@Injectable({
  providedIn: 'root',
})
export class ModelService {
  private apiUrl = `${environment.apiUrl}/models`;

  constructor(private http: HttpClient) { }

  getSalientsModels(): Observable<Model[]> {
    return this.http.get<Model[]>(`${this.apiUrl}/salients-models`);
  }

  getAllModels(): Observable<Model[]> {
    return this.http.get<Model[]>(`${this.apiUrl}`);
  }

  getModelByName(modelName: string): Observable<Model[]> {
    return this.http.get<Model[]>(`${this.apiUrl}/name`, {
      params: { name: modelName }
    });
  }

  // Admin methods
  getModelById(id: number): Observable<Model> {
    const token = localStorage.getItem('accessToken');
    return this.http.get<Model>(`${this.apiUrl}/${id}`, {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${token}`
      })
    });
  }

  createModel(name: string, biography: string, file: File): Observable<Model> {
    const formData = new FormData();
    formData.append('name', name);
    formData.append('biography', biography);
    formData.append('profile', file);

    const token = localStorage.getItem('accessToken');
    return this.http.post<Model>(`${this.apiUrl}`, formData, {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${token}`
      })
    });
  }

  updateModel(id: number, name: string, biography: string, file: File): Observable<Model> {
    const formData = new FormData();
    formData.append('name', name);
    formData.append('biography', biography);
    formData.append('profile', file);

    const token = localStorage.getItem('accessToken');
    return this.http.put<Model>(`${this.apiUrl}/${id}`, formData, {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${token}`
      })
    });
  }

  deleteModel(id: number): Observable<void> {
    const token = localStorage.getItem('accessToken');
    return this.http.delete<void>(`${this.apiUrl}/${id}`, {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${token}`
      })
    });
  }
}
