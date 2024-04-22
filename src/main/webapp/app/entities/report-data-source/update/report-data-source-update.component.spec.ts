import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ReportDataSourceService } from '../service/report-data-source.service';
import { IReportDataSource } from '../report-data-source.model';
import { ReportDataSourceFormService } from './report-data-source-form.service';

import { ReportDataSourceUpdateComponent } from './report-data-source-update.component';

describe('ReportDataSource Management Update Component', () => {
  let comp: ReportDataSourceUpdateComponent;
  let fixture: ComponentFixture<ReportDataSourceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reportDataSourceFormService: ReportDataSourceFormService;
  let reportDataSourceService: ReportDataSourceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ReportDataSourceUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ReportDataSourceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReportDataSourceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reportDataSourceFormService = TestBed.inject(ReportDataSourceFormService);
    reportDataSourceService = TestBed.inject(ReportDataSourceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const reportDataSource: IReportDataSource = { rid: 'CBA' };

      activatedRoute.data = of({ reportDataSource });
      comp.ngOnInit();

      expect(comp.reportDataSource).toEqual(reportDataSource);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportDataSource>>();
      const reportDataSource = { rid: 'ABC' };
      jest.spyOn(reportDataSourceFormService, 'getReportDataSource').mockReturnValue(reportDataSource);
      jest.spyOn(reportDataSourceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportDataSource });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportDataSource }));
      saveSubject.complete();

      // THEN
      expect(reportDataSourceFormService.getReportDataSource).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reportDataSourceService.update).toHaveBeenCalledWith(expect.objectContaining(reportDataSource));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportDataSource>>();
      const reportDataSource = { rid: 'ABC' };
      jest.spyOn(reportDataSourceFormService, 'getReportDataSource').mockReturnValue({ rid: null });
      jest.spyOn(reportDataSourceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportDataSource: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportDataSource }));
      saveSubject.complete();

      // THEN
      expect(reportDataSourceFormService.getReportDataSource).toHaveBeenCalled();
      expect(reportDataSourceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportDataSource>>();
      const reportDataSource = { rid: 'ABC' };
      jest.spyOn(reportDataSourceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportDataSource });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reportDataSourceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
