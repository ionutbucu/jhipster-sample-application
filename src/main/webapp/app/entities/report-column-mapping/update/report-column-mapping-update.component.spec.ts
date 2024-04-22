import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IReport } from 'app/entities/report/report.model';
import { ReportService } from 'app/entities/report/service/report.service';
import { ReportColumnMappingService } from '../service/report-column-mapping.service';
import { IReportColumnMapping } from '../report-column-mapping.model';
import { ReportColumnMappingFormService } from './report-column-mapping-form.service';

import { ReportColumnMappingUpdateComponent } from './report-column-mapping-update.component';

describe('ReportColumnMapping Management Update Component', () => {
  let comp: ReportColumnMappingUpdateComponent;
  let fixture: ComponentFixture<ReportColumnMappingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reportColumnMappingFormService: ReportColumnMappingFormService;
  let reportColumnMappingService: ReportColumnMappingService;
  let reportService: ReportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ReportColumnMappingUpdateComponent],
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
      .overrideTemplate(ReportColumnMappingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReportColumnMappingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reportColumnMappingFormService = TestBed.inject(ReportColumnMappingFormService);
    reportColumnMappingService = TestBed.inject(ReportColumnMappingService);
    reportService = TestBed.inject(ReportService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Report query and add missing value', () => {
      const reportColumnMapping: IReportColumnMapping = { rid: 'CBA' };
      const report: IReport = { rid: '9f895995-ec6b-48da-9942-b6350e99b87c' };
      reportColumnMapping.report = report;

      const reportCollection: IReport[] = [{ rid: 'f0885029-5463-4cc1-8ebb-69fcce6682e6' }];
      jest.spyOn(reportService, 'query').mockReturnValue(of(new HttpResponse({ body: reportCollection })));
      const additionalReports = [report];
      const expectedCollection: IReport[] = [...additionalReports, ...reportCollection];
      jest.spyOn(reportService, 'addReportToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportColumnMapping });
      comp.ngOnInit();

      expect(reportService.query).toHaveBeenCalled();
      expect(reportService.addReportToCollectionIfMissing).toHaveBeenCalledWith(
        reportCollection,
        ...additionalReports.map(expect.objectContaining),
      );
      expect(comp.reportsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const reportColumnMapping: IReportColumnMapping = { rid: 'CBA' };
      const report: IReport = { rid: 'cb9472db-3590-4b9e-83be-4b77b1394123' };
      reportColumnMapping.report = report;

      activatedRoute.data = of({ reportColumnMapping });
      comp.ngOnInit();

      expect(comp.reportsSharedCollection).toContain(report);
      expect(comp.reportColumnMapping).toEqual(reportColumnMapping);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportColumnMapping>>();
      const reportColumnMapping = { rid: 'ABC' };
      jest.spyOn(reportColumnMappingFormService, 'getReportColumnMapping').mockReturnValue(reportColumnMapping);
      jest.spyOn(reportColumnMappingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportColumnMapping });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportColumnMapping }));
      saveSubject.complete();

      // THEN
      expect(reportColumnMappingFormService.getReportColumnMapping).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reportColumnMappingService.update).toHaveBeenCalledWith(expect.objectContaining(reportColumnMapping));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportColumnMapping>>();
      const reportColumnMapping = { rid: 'ABC' };
      jest.spyOn(reportColumnMappingFormService, 'getReportColumnMapping').mockReturnValue({ rid: null });
      jest.spyOn(reportColumnMappingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportColumnMapping: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportColumnMapping }));
      saveSubject.complete();

      // THEN
      expect(reportColumnMappingFormService.getReportColumnMapping).toHaveBeenCalled();
      expect(reportColumnMappingService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportColumnMapping>>();
      const reportColumnMapping = { rid: 'ABC' };
      jest.spyOn(reportColumnMappingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportColumnMapping });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reportColumnMappingService.update).toHaveBeenCalled();
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
