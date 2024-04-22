import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ReportScheduleDetailComponent } from './report-schedule-detail.component';

describe('ReportSchedule Management Detail Component', () => {
  let comp: ReportScheduleDetailComponent;
  let fixture: ComponentFixture<ReportScheduleDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportScheduleDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ReportScheduleDetailComponent,
              resolve: { reportSchedule: () => of({ rid: 'ABC' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ReportScheduleDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReportScheduleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load reportSchedule on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ReportScheduleDetailComponent);

      // THEN
      expect(instance.reportSchedule()).toEqual(expect.objectContaining({ rid: 'ABC' }));
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
