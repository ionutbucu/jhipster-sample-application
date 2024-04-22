import dayjs from 'dayjs/esm';

import { IReportExecution, NewReportExecution } from './report-execution.model';

export const sampleWithRequiredData: IReportExecution = {
  rid: '9dbb3b75-1c58-48c9-ace8-b01f279888a0',
  date: dayjs('2024-04-21T16:57'),
};

export const sampleWithPartialData: IReportExecution = {
  rid: 'cbf5b21e-7e72-4ac5-b3c5-6ea71af2a344',
  date: dayjs('2024-04-21T22:28'),
  url: 'https://frequent-vice.org/',
  user: 'despite',
  additionalInfo: 'than',
};

export const sampleWithFullData: IReportExecution = {
  rid: '1c257370-d826-4b20-b347-a4385c7b55c5',
  date: dayjs('2024-04-22T12:33'),
  error: 'solidly scarcely defame',
  url: 'https://salty-member.com',
  user: 'supposing offset',
  additionalInfo: 'afore eventuate',
};

export const sampleWithNewData: NewReportExecution = {
  date: dayjs('2024-04-21T20:02'),
  rid: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
