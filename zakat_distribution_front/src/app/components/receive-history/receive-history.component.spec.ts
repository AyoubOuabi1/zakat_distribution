import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReceiveHistoryComponent } from './receive-history.component';

describe('ReceiveHistoryComponent', () => {
  let component: ReceiveHistoryComponent;
  let fixture: ComponentFixture<ReceiveHistoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReceiveHistoryComponent]
    });
    fixture = TestBed.createComponent(ReceiveHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
