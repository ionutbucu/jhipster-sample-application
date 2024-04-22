jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ReportScheduleService } from '../service/report-schedule.service';

import { ReportScheduleDeleteDialogComponent } from './report-schedule-delete-dialog.component';

describe('ReportSchedule Management Delete Component', () => {
  let comp: ReportScheduleDeleteDialogComponent;
  let fixture: ComponentFixture<ReportScheduleDeleteDialogComponent>;
  let service: ReportScheduleService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ReportScheduleDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(ReportScheduleDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ReportScheduleDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ReportScheduleService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete('ABC');
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith('ABC');
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
