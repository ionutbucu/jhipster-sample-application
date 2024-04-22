import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IReportDistribution, NewReportDistribution } from '../report-distribution.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { rid: unknown }> = Partial<Omit<T, 'rid'>> & { rid: T['rid'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReportDistribution for edit and NewReportDistributionFormGroupInput for create.
 */
type ReportDistributionFormGroupInput = IReportDistribution | PartialWithRequiredKeyOf<NewReportDistribution>;

type ReportDistributionFormDefaults = Pick<NewReportDistribution, 'rid'>;

type ReportDistributionFormGroupContent = {
  rid: FormControl<IReportDistribution['rid'] | NewReportDistribution['rid']>;
  email: FormControl<IReportDistribution['email']>;
  description: FormControl<IReportDistribution['description']>;
  report: FormControl<IReportDistribution['report']>;
};

export type ReportDistributionFormGroup = FormGroup<ReportDistributionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReportDistributionFormService {
  createReportDistributionFormGroup(reportDistribution: ReportDistributionFormGroupInput = { rid: null }): ReportDistributionFormGroup {
    const reportDistributionRawValue = {
      ...this.getFormDefaults(),
      ...reportDistribution,
    };
    return new FormGroup<ReportDistributionFormGroupContent>({
      rid: new FormControl(
        { value: reportDistributionRawValue.rid, disabled: reportDistributionRawValue.rid !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      email: new FormControl(reportDistributionRawValue.email, {
        validators: [Validators.required, Validators.minLength(3), Validators.pattern('^[^@]+@[^@]+$')],
      }),
      description: new FormControl(reportDistributionRawValue.description, {
        validators: [Validators.maxLength(256)],
      }),
      report: new FormControl(reportDistributionRawValue.report),
    });
  }

  getReportDistribution(form: ReportDistributionFormGroup): IReportDistribution | NewReportDistribution {
    return form.getRawValue() as IReportDistribution | NewReportDistribution;
  }

  resetForm(form: ReportDistributionFormGroup, reportDistribution: ReportDistributionFormGroupInput): void {
    const reportDistributionRawValue = { ...this.getFormDefaults(), ...reportDistribution };
    form.reset(
      {
        ...reportDistributionRawValue,
        rid: { value: reportDistributionRawValue.rid, disabled: reportDistributionRawValue.rid !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReportDistributionFormDefaults {
    return {
      rid: null,
    };
  }
}
