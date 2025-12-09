import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { Login } from './pages/login/login';
import { FormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { Register } from './pages/register/register';
import { Home } from './pages/home/home';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faFacebook, faInstagram, faXTwitter } from '@fortawesome/free-brands-svg-icons';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { VideoDetail } from './pages/video-detail/video-detail';
import { ModelProfile } from './pages/model-profile/model-profile';
import { VideoGallery } from './pages/video-gallery/video-gallery';
import { ModelGallery } from './pages/model-gallery/model-gallery';
import { Notifications } from './pages/notification/notification';
import { Library } from './pages/library/library';
import { AdminPanel } from './pages/admin-panel/admin-panel';
import { FooterComponent } from './components/footer/footer';

@NgModule({
  declarations: [
    App,
    Login,
    Register,
    Home,
    VideoDetail,
    ModelProfile,
    VideoGallery,
    ModelGallery,
    Notifications,
    Library,
    AdminPanel
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    FontAwesomeModule,
    FooterComponent
  ],
  providers: [
    provideBrowserGlobalErrorListeners()
  ],
  bootstrap: [App]
})
export class AppModule {
  constructor(library: FaIconLibrary) {
    library.addIcons(faFacebook, faXTwitter, faInstagram)
  }
}
