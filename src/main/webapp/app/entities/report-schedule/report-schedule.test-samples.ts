import { IReportSchedule, NewReportSchedule } from './report-schedule.model';

export const sampleWithRequiredData: IReportSchedule = {
  rid: '61c2b0fc-54f5-47ca-b4e7-9e07ae6bc1d2',
  cron: 'vaguely oh',
};

export const sampleWithPartialData: IReportSchedule = {
  rid: '6cc8992f-178b-4459-91fb-6295c85423e5',
  cron: 'trained satire',
};

export const sampleWithFullData: IReportSchedule = {
  rid: '6d55b77d-f117-42e1-84e5-02618ce0a536',
  cron: 'gracious',
};

export const sampleWithNewData: NewReportSchedule = {
  cron: 'beneficiary stultify',
  rid: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
