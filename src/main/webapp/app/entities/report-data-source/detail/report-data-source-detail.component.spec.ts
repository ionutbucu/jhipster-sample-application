import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ReportDataSourceDetailComponent } from './report-data-source-detail.component';

describe('ReportDataSource Management Detail Component', () => {
  let comp: ReportDataSourceDetailComponent;
  let fixture: ComponentFixture<ReportDataSourceDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportDataSourceDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ReportDataSourceDetailComponent,
              resolve: { reportDataSource: () => of({ rid: 'ABC' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ReportDataSourceDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReportDataSourceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load reportDataSource on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ReportDataSourceDetailComponent);

      // THEN
      expect(instance.reportDataSource()).toEqual(expect.objectContaining({ rid: 'ABC' }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
