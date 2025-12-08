import { Component, OnInit } from '@angular/core';
import { NotificationService, Notification } from '../../services/notifications/notification-service';
import { AuthService } from '../../services/auth/authService';

@Component({
  selector: 'app-notification',
  standalone: false,
  templateUrl: './notification.html',
  styleUrl: './notification.scss',
})
export class Notifications implements OnInit {

  notificationsList: Notification[] = [];
  isLoggedIn: boolean = false;
  userId: number | null = null;

  constructor(
    private notificationService: NotificationService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedIn();
    if (this.isLoggedIn) {
      this.userId = this.authService.getUserId();
      if (this.userId) {
        this.loadNotifications();
      }
    }
  }

  loadNotifications() {
    if (this.userId) {
      this.notificationService.getNotifications(this.userId).subscribe({
        next: (data) => {
          this.notificationsList = data;
        },
        error: (err) => {
          console.error('Error loading notifications:', err);
        }
      });
    }
  }

  // Formatea la fecha y hora para una visualización amigable
  formatDate(dateString: string): string {
    const date = new Date(dateString);
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
    if (this.userId) {
      this.notificationService.deleteAllNotifications(this.userId).subscribe({
        next: () => {
          this.notificationsList = [];
        },
        error: (err) => {
          console.error('Error clearing notifications:', err);
        }
      });
    }
  }

  deleteNotification(id: number) {
    this.notificationService.deleteNotification(id).subscribe({
      next: () => {
        this.notificationsList = this.notificationsList.filter((notif) => notif.id !== id);
      },
      error: (err) => {
        console.error('Error deleting notification:', err);
      }
    });
  }
}
