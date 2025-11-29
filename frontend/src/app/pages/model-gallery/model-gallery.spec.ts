import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModelGallery } from './model-gallery';

describe('ModelGallery', () => {
  let component: ModelGallery;
  let fixture: ComponentFixture<ModelGallery>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ModelGallery]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModelGallery);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
