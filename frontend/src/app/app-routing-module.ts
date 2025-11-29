import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { Register } from './pages/register/register';
import { Home } from './pages/home/home';
import { VideoDetail } from './pages/video-detail/video-detail';
import { ModelProfile } from './pages/model-profile/model-profile';
import { VideoGallery } from './pages/video-gallery/video-gallery';
import { ModelGallery } from './pages/model-gallery/model-gallery';
import { Notifications } from './pages/notification/notification';
import { Library } from './pages/library/library';

const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: 'home', component: Home },
  { path: 'video/:id', component: VideoDetail },
  { path: 'model/:id', component: ModelProfile },
  { path: 'video-gallery', component: VideoGallery },
  { path: 'model-gallery', component: ModelGallery },
  { path: 'notification', component: Notifications },
  { path: 'library', component: Library },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
