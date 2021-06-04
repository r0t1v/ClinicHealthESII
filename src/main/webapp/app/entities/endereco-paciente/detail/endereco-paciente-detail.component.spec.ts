import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EnderecoPacienteDetailComponent } from './endereco-paciente-detail.component';

describe('Component Tests', () => {
  describe('EnderecoPaciente Management Detail Component', () => {
    let comp: EnderecoPacienteDetailComponent;
    let fixture: ComponentFixture<EnderecoPacienteDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [EnderecoPacienteDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ enderecoPaciente: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(EnderecoPacienteDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EnderecoPacienteDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load enderecoPaciente on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.enderecoPaciente).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
