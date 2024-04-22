import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ReportMetadataService } from '../service/report-metadata.service';
import { IReportMetadata } from '../report-metadata.model';
import { ReportMetadataFormService } from './report-metadata-form.service';

import { ReportMetadataUpdateComponent } from './report-metadata-update.component';

describe('ReportMetadata Management Update Component', () => {
  let comp: ReportMetadataUpdateComponent;
  let fixture: ComponentFixture<ReportMetadataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reportMetadataFormService: ReportMetadataFormService;
  let reportMetadataService: ReportMetadataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ReportMetadataUpdateComponent],
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
      .overrideTemplate(ReportMetadataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReportMetadataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reportMetadataFormService = TestBed.inject(ReportMetadataFormService);
    reportMetadataService = TestBed.inject(ReportMetadataService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const reportMetadata: IReportMetadata = { rid: 'CBA' };

      activatedRoute.data = of({ reportMetadata });
      comp.ngOnInit();

      expect(comp.reportMetadata).toEqual(reportMetadata);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportMetadata>>();
      const reportMetadata = { rid: 'ABC' };
      jest.spyOn(reportMetadataFormService, 'getReportMetadata').mockReturnValue(reportMetadata);
      jest.spyOn(reportMetadataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportMetadata });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportMetadata }));
      saveSubject.complete();

      // THEN
      expect(reportMetadataFormService.getReportMetadata).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reportMetadataService.update).toHaveBeenCalledWith(expect.objectContaining(reportMetadata));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportMetadata>>();
      const reportMetadata = { rid: 'ABC' };
      jest.spyOn(reportMetadataFormService, 'getReportMetadata').mockReturnValue({ rid: null });
      jest.spyOn(reportMetadataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportMetadata: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportMetadata }));
      saveSubject.complete();

      // THEN
      expect(reportMetadataFormService.getReportMetadata).toHaveBeenCalled();
      expect(reportMetadataService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportMetadata>>();
      const reportMetadata = { rid: 'ABC' };
      jest.spyOn(reportMetadataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportMetadata });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reportMetadataService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
