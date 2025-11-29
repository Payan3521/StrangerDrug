import { Component, OnInit } from '@angular/core';

interface Notification {
  id: number;
  type: 'sale' | 'purchase' | 'system'; // Tipo de notificación
  title: string;
  message: string;
  date: Date;
  // Propiedades opcionales específicas de transacciones
  videoTitle?: string;
  clientName?: string; // Solo para Admin (type: 'sale')
  price?: number;
}

@Component({
  selector: 'app-notification',
  standalone: false,
  templateUrl: './notification.html',
  styleUrl: './notification.scss',
})
export class Notifications implements OnInit {

  // Simulación de rol (para mostrar diferentes tipos de notificaciones)
  userRole: 'admin' | 'client' = 'client'; // Puedes cambiar a 'admin' para probar

  notificationsList: Notification[] = [];

  constructor() { }

  ngOnInit(): void {
    this.loadNotifications();
  }

  loadNotifications() {
    if (this.userRole === 'client') {
      this.notificationsList = this.loadClientNotifications();
    } else {
      this.notificationsList = this.loadAdminNotifications();
    }
  }

  // --- Datos de Simulación para Cliente (Notificación de Compra) ---
  loadClientNotifications(): Notification[] {
    return [
  {
    id: 1,
    type: 'purchase',
    title: '¡Compra Exitosa!',
    message: 'Has comprado el video "Sesión Tropical".',
    videoTitle: 'Sesión Tropical',
    price: 12.99,
    date: new Date(2025, 10, 26, 15, 30),
  },
  {
    id: 2,
    type: 'purchase',
    title: '¡Transacción Completada!',
    message: 'La compra de "Lencería Roja" ha sido procesada.',
    videoTitle: 'Lencería Roja',
    price: 9.99,
    date: new Date(2025, 10, 25, 9, 15),
  },
  {
    id: 3,
    type: 'system',
    title: 'Nueva Modelo Disponible',
    message: '¡Natalia Reyes ha subido 5 videos nuevos! Échales un vistazo.',
    date: new Date(2025, 10, 20, 18, 0),
  },
] as const;
  }

  // --- Datos de Simulación para Administrador (Notificación de Venta) ---
  loadAdminNotifications(): Notification[] {
return [
  {
    id: 101,
    type: 'sale',
    title: '¡Nueva Venta Realizada!',
    message: 'Venta de "Sesión Tropical" al cliente Sofía C.',
    clientName: 'Sofía C.',
    videoTitle: 'Sesión Tropical',
    price: 12.99,
    date: new Date(2025, 10, 27, 21, 0),
  },
  {
    id: 102,
    type: 'sale',
    title: 'Venta Procesada',
    message: 'Venta de "Gimnasio Caliente" al cliente Juan P.',
    clientName: 'Juan P.',
    videoTitle: 'Gimnasio Caliente',
    price: 15.99,
    date: new Date(2025, 10, 27, 10, 45),
  },
  {
    id: 103,
    type: 'sale',
    title: 'Venta Procesada',
    message: 'Venta de "Lencería Roja" al cliente Laura R.',
    clientName: 'Laura R.',
    videoTitle: 'Lencería Roja',
    price: 9.99,
    date: new Date(2025, 10, 26, 15, 30),
  },
  {
    id: 104,
    type: 'system',
    title: 'Mantenimiento Programado',
    message: 'Recuerda que el servidor principal tendrá mantenimiento esta noche.',
    date: new Date(2025, 10, 25, 8, 0),
  },
] as const;

  }

  // Formatea la fecha y hora para una visualización amigable
  formatDate(date: Date): string {
    const options: Intl.DateTimeFormatOptions = { 
        year: 'numeric', 
        month: 'short', 
        day: 'numeric', 
        hour: '2-digit', 
        minute: '2-digit' 
    };
    return date.toLocaleDateString('es-ES', options);
  }

  clearAllNotifications() {
    this.notificationsList = [];
  }

  deleteNotification(id: number) {
    this.notificationsList = this.notificationsList.filter((notif) => notif.id !== id);
  }
}
