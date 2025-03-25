import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FinancialHistoryComponent } from './financial-history.component';

describe('FinancialHistoryComponent', () => {
  let component: FinancialHistoryComponent;
  let fixture: ComponentFixture<FinancialHistoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FinancialHistoryComponent]
    });
    fixture = TestBed.createComponent(FinancialHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
