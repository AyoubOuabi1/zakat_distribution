import { TestBed } from '@angular/core/testing';

import { ReceiveService } from './receive.service';

describe('ReceiveService', () => {
  let service: ReceiveService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReceiveService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
