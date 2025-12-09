import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface PaymentDto {
  id: number;
  transactionId: string;
  status: string;
  amount: number;
  createdAt: string;
}

export interface UserDto {
  id: number;
  name: string;
  email: string;
  birthdate: string;
  role: string;
}

export interface VideoDto {
  id: number;
  s3Bucket: string;
  s3Key: string;
  type: string;
  s3Url?: string;
}

export interface PurchaseResponseDto {
  id: number;
  createdAt: string;
  payment: PaymentDto;
  user: UserDto;
  video: VideoDto;
  statusPurchaseAdmin: boolean;
  statusPurchaseCliente: boolean;
  amount: number;
}

export interface PurchaseCreateDto {
  buyerUserId: number;
  videoKey: string;
  paymentId: number;
  amount: number;
}

@Injectable({
  providedIn: 'root',
})
export class PurchaseService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('accessToken');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  createPurchase(purchase: PurchaseCreateDto): Observable<PurchaseResponseDto> {
    return this.http.post<PurchaseResponseDto>(
      `${this.apiUrl}/purchases`,
      purchase,
      { headers: this.getHeaders() }
    );
  }

  getPurchasesByUserId(userId: number): Observable<PurchaseResponseDto[]> {
    return this.http.get<PurchaseResponseDto[]>(
      `${this.apiUrl}/purchases/buyerUser/${userId}`,
      { headers: this.getHeaders() }
    );
  }

  softDeletePurchase(purchaseId: number): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/soft-delete-cliente/${purchaseId}`,
      { headers: this.getHeaders() }
    );
  }

  // Admin methods
  getAllPurchases(): Observable<PurchaseResponseDto[]> {
    return this.http.get<PurchaseResponseDto[]>(
      `${this.apiUrl}/purchases`,
      { headers: this.getHeaders() }
    );
  }

  getPurchaseById(id: number): Observable<PurchaseResponseDto> {
    return this.http.get<PurchaseResponseDto>(
      `${this.apiUrl}/purchases/${id}`,
      { headers: this.getHeaders() }
    );
  }

  softDeleteAdmin(purchaseId: number): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/purchases/soft-delete-admin/${purchaseId}`,
      { headers: this.getHeaders() }
    );
  }

  softDeleteAllAdmin(): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/purchases/soft-delete-clear-admin`,
      { headers: this.getHeaders() }
    );
  }
}
