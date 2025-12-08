import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Notification {
  id: number;
  message: string;
  createdAt: string;
  purchase?: any;
  senderUser?: any;
  receiverUser?: any;
}

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('accessToken');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  getNotifications(userId: number): Observable<Notification[]> {
    return this.http.get<Notification[]>(`${this.apiUrl}/notifications/${userId}`, { headers: this.getHeaders() });
  }

  deleteNotification(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/notifications/${id}`, { headers: this.getHeaders() });
  }

  deleteAllNotifications(userId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/notifications/clear/${userId}`, { headers: this.getHeaders() });
  }
}
