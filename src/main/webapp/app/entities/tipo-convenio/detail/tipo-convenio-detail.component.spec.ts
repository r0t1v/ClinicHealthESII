import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TipoConvenioDetailComponent } from './tipo-convenio-detail.component';

describe('Component Tests', () => {
  describe('TipoConvenio Management Detail Component', () => {
    let comp: TipoConvenioDetailComponent;
    let fixture: ComponentFixture<TipoConvenioDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TipoConvenioDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ tipoConvenio: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TipoConvenioDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TipoConvenioDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load tipoConvenio on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tipoConvenio).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
