import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IReport } from 'app/entities/report/report.model';
import { ReportService } from 'app/entities/report/service/report.service';
import { ReportParamService } from '../service/report-param.service';
import { IReportParam } from '../report-param.model';
import { ReportParamFormService } from './report-param-form.service';

import { ReportParamUpdateComponent } from './report-param-update.component';

describe('ReportParam Management Update Component', () => {
  let comp: ReportParamUpdateComponent;
  let fixture: ComponentFixture<ReportParamUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reportParamFormService: ReportParamFormService;
  let reportParamService: ReportParamService;
  let reportService: ReportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ReportParamUpdateComponent],
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
      .overrideTemplate(ReportParamUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReportParamUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reportParamFormService = TestBed.inject(ReportParamFormService);
    reportParamService = TestBed.inject(ReportParamService);
    reportService = TestBed.inject(ReportService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Report query and add missing value', () => {
      const reportParam: IReportParam = { rid: 'CBA' };
      const report: IReport = { rid: 'f73a584b-e6d2-4ee9-bbb6-559a4b2556ee' };
      reportParam.report = report;

      const reportCollection: IReport[] = [{ rid: '3126d906-507e-4296-982b-1a67e05d50ba' }];
      jest.spyOn(reportService, 'query').mockReturnValue(of(new HttpResponse({ body: reportCollection })));
      const additionalReports = [report];
      const expectedCollection: IReport[] = [...additionalReports, ...reportCollection];
      jest.spyOn(reportService, 'addReportToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportParam });
      comp.ngOnInit();

      expect(reportService.query).toHaveBeenCalled();
      expect(reportService.addReportToCollectionIfMissing).toHaveBeenCalledWith(
        reportCollection,
        ...additionalReports.map(expect.objectContaining),
      );
      expect(comp.reportsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const reportParam: IReportParam = { rid: 'CBA' };
      const report: IReport = { rid: '4e89cbef-794f-4e55-b046-d6124c47cc50' };
      reportParam.report = report;

      activatedRoute.data = of({ reportParam });
      comp.ngOnInit();

      expect(comp.reportsSharedCollection).toContain(report);
      expect(comp.reportParam).toEqual(reportParam);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportParam>>();
      const reportParam = { rid: 'ABC' };
      jest.spyOn(reportParamFormService, 'getReportParam').mockReturnValue(reportParam);
      jest.spyOn(reportParamService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportParam });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportParam }));
      saveSubject.complete();

      // THEN
      expect(reportParamFormService.getReportParam).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reportParamService.update).toHaveBeenCalledWith(expect.objectContaining(reportParam));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportParam>>();
      const reportParam = { rid: 'ABC' };
      jest.spyOn(reportParamFormService, 'getReportParam').mockReturnValue({ rid: null });
      jest.spyOn(reportParamService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportParam: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportParam }));
      saveSubject.complete();

      // THEN
      expect(reportParamFormService.getReportParam).toHaveBeenCalled();
      expect(reportParamService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportParam>>();
      const reportParam = { rid: 'ABC' };
      jest.spyOn(reportParamService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportParam });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reportParamService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareReport', () => {
      it('Should forward to reportService', () => {
        const entity = { rid: 'ABC' };
        const entity2 = { rid: 'CBA' };
        jest.spyOn(reportService, 'compareReport');
        comp.compareReport(entity, entity2);
        expect(reportService.compareReport).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
