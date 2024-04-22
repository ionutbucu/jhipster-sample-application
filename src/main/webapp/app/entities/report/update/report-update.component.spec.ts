import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IReportDataSource } from 'app/entities/report-data-source/report-data-source.model';
import { ReportDataSourceService } from 'app/entities/report-data-source/service/report-data-source.service';
import { IReportMetadata } from 'app/entities/report-metadata/report-metadata.model';
import { ReportMetadataService } from 'app/entities/report-metadata/service/report-metadata.service';
import { IReport } from '../report.model';
import { ReportService } from '../service/report.service';
import { ReportFormService } from './report-form.service';

import { ReportUpdateComponent } from './report-update.component';

describe('Report Management Update Component', () => {
  let comp: ReportUpdateComponent;
  let fixture: ComponentFixture<ReportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reportFormService: ReportFormService;
  let reportService: ReportService;
  let reportDataSourceService: ReportDataSourceService;
  let reportMetadataService: ReportMetadataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ReportUpdateComponent],
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
      .overrideTemplate(ReportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reportFormService = TestBed.inject(ReportFormService);
    reportService = TestBed.inject(ReportService);
    reportDataSourceService = TestBed.inject(ReportDataSourceService);
    reportMetadataService = TestBed.inject(ReportMetadataService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call datasource query and add missing value', () => {
      const report: IReport = { rid: 'CBA' };
      const datasource: IReportDataSource = { rid: '9966c66e-90f8-4b49-bd93-2f65f193f8d6' };
      report.datasource = datasource;

      const datasourceCollection: IReportDataSource[] = [{ rid: '35050d20-6c88-496d-8a5c-01430b52ff67' }];
      jest.spyOn(reportDataSourceService, 'query').mockReturnValue(of(new HttpResponse({ body: datasourceCollection })));
      const expectedCollection: IReportDataSource[] = [datasource, ...datasourceCollection];
      jest.spyOn(reportDataSourceService, 'addReportDataSourceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ report });
      comp.ngOnInit();

      expect(reportDataSourceService.query).toHaveBeenCalled();
      expect(reportDataSourceService.addReportDataSourceToCollectionIfMissing).toHaveBeenCalledWith(datasourceCollection, datasource);
      expect(comp.datasourcesCollection).toEqual(expectedCollection);
    });

    it('Should call metadata query and add missing value', () => {
      const report: IReport = { rid: 'CBA' };
      const metadata: IReportMetadata = { rid: 'fc267eb0-9365-4022-89b1-7a54a3a8ad50' };
      report.metadata = metadata;

      const metadataCollection: IReportMetadata[] = [{ rid: 'ad2f4181-1bcc-456a-8478-4b846212cd6d' }];
      jest.spyOn(reportMetadataService, 'query').mockReturnValue(of(new HttpResponse({ body: metadataCollection })));
      const expectedCollection: IReportMetadata[] = [metadata, ...metadataCollection];
      jest.spyOn(reportMetadataService, 'addReportMetadataToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ report });
      comp.ngOnInit();

      expect(reportMetadataService.query).toHaveBeenCalled();
      expect(reportMetadataService.addReportMetadataToCollectionIfMissing).toHaveBeenCalledWith(metadataCollection, metadata);
      expect(comp.metadataCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const report: IReport = { rid: 'CBA' };
      const datasource: IReportDataSource = { rid: '31505155-3d2b-4165-bb89-5e6fe91fcdc1' };
      report.datasource = datasource;
      const metadata: IReportMetadata = { rid: '33ffdc29-25ba-4919-a54c-396f21ee60a9' };
      report.metadata = metadata;

      activatedRoute.data = of({ report });
      comp.ngOnInit();

      expect(comp.datasourcesCollection).toContain(datasource);
      expect(comp.metadataCollection).toContain(metadata);
      expect(comp.report).toEqual(report);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReport>>();
      const report = { rid: 'ABC' };
      jest.spyOn(reportFormService, 'getReport').mockReturnValue(report);
      jest.spyOn(reportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ report });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: report }));
      saveSubject.complete();

      // THEN
      expect(reportFormService.getReport).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reportService.update).toHaveBeenCalledWith(expect.objectContaining(report));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReport>>();
      const report = { rid: 'ABC' };
      jest.spyOn(reportFormService, 'getReport').mockReturnValue({ rid: null });
      jest.spyOn(reportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ report: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: report }));
      saveSubject.complete();

      // THEN
      expect(reportFormService.getReport).toHaveBeenCalled();
      expect(reportService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReport>>();
      const report = { rid: 'ABC' };
      jest.spyOn(reportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ report });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reportService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareReportDataSource', () => {
      it('Should forward to reportDataSourceService', () => {
        const entity = { rid: 'ABC' };
        const entity2 = { rid: 'CBA' };
        jest.spyOn(reportDataSourceService, 'compareReportDataSource');
        comp.compareReportDataSource(entity, entity2);
        expect(reportDataSourceService.compareReportDataSource).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareReportMetadata', () => {
      it('Should forward to reportMetadataService', () => {
        const entity = { rid: 'ABC' };
        const entity2 = { rid: 'CBA' };
        jest.spyOn(reportMetadataService, 'compareReportMetadata');
        comp.compareReportMetadata(entity, entity2);
        expect(reportMetadataService.compareReportMetadata).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
