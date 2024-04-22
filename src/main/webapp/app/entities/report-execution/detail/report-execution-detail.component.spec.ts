import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ReportExecutionDetailComponent } from './report-execution-detail.component';

describe('ReportExecution Management Detail Component', () => {
  let comp: ReportExecutionDetailComponent;
  let fixture: ComponentFixture<ReportExecutionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportExecutionDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ReportExecutionDetailComponent,
              resolve: { reportExecution: () => of({ rid: 'ABC' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ReportExecutionDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReportExecutionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load reportExecution on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ReportExecutionDetailComponent);

      // THEN
      expect(instance.reportExecution()).toEqual(expect.objectContaining({ rid: 'ABC' }));
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
