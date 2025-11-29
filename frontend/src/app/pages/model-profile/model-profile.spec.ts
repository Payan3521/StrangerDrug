import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModelProfile } from './model-profile';

describe('ModelProfile', () => {
  let component: ModelProfile;
  let fixture: ComponentFixture<ModelProfile>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ModelProfile]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModelProfile);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
