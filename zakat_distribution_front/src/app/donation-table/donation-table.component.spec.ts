import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DonationTableComponent } from './donation-table.component';

describe('DonationTableComponent', () => {
  let component: DonationTableComponent;
  let fixture: ComponentFixture<DonationTableComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DonationTableComponent]
    });
    fixture = TestBed.createComponent(DonationTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
