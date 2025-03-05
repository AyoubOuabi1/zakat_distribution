import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DonationHistoryComponent } from './donation-history.component';

describe('DonationHistoryComponent', () => {
  let component: DonationHistoryComponent;
  let fixture: ComponentFixture<DonationHistoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DonationHistoryComponent]
    });
    fixture = TestBed.createComponent(DonationHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
