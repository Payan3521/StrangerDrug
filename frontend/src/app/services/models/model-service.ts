import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
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
}
