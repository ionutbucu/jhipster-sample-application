jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ReportDataSourceService } from '../service/report-data-source.service';

import { ReportDataSourceDeleteDialogComponent } from './report-data-source-delete-dialog.component';

describe('ReportDataSource Management Delete Component', () => {
  let comp: ReportDataSourceDeleteDialogComponent;
  let fixture: ComponentFixture<ReportDataSourceDeleteDialogComponent>;
  let service: ReportDataSourceService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ReportDataSourceDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(ReportDataSourceDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ReportDataSourceDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ReportDataSourceService);
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
