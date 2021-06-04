import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITipoConvenio, TipoConvenio } from '../tipo-convenio.model';

import { TipoConvenioService } from './tipo-convenio.service';

describe('Service Tests', () => {
  describe('TipoConvenio Service', () => {
    let service: TipoConvenioService;
    let httpMock: HttpTestingController;
    let elemDefault: ITipoConvenio;
    let expectedResult: ITipoConvenio | ITipoConvenio[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TipoConvenioService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        convenio: 'AAAAAAA',
        codigoconvenio: 0,
        nomeconvenio: 'AAAAAAA',
        contato: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a TipoConvenio', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new TipoConvenio()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TipoConvenio', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            convenio: 'BBBBBB',
            codigoconvenio: 1,
            nomeconvenio: 'BBBBBB',
            contato: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a TipoConvenio', () => {
        const patchObject = Object.assign(
          {
            nomeconvenio: 'BBBBBB',
          },
          new TipoConvenio()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TipoConvenio', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            convenio: 'BBBBBB',
            codigoconvenio: 1,
            nomeconvenio: 'BBBBBB',
            contato: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a TipoConvenio', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTipoConvenioToCollectionIfMissing', () => {
        it('should add a TipoConvenio to an empty array', () => {
          const tipoConvenio: ITipoConvenio = { id: 123 };
          expectedResult = service.addTipoConvenioToCollectionIfMissing([], tipoConvenio);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tipoConvenio);
        });

        it('should not add a TipoConvenio to an array that contains it', () => {
          const tipoConvenio: ITipoConvenio = { id: 123 };
          const tipoConvenioCollection: ITipoConvenio[] = [
            {
              ...tipoConvenio,
            },
            { id: 456 },
          ];
          expectedResult = service.addTipoConvenioToCollectionIfMissing(tipoConvenioCollection, tipoConvenio);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TipoConvenio to an array that doesn't contain it", () => {
          const tipoConvenio: ITipoConvenio = { id: 123 };
          const tipoConvenioCollection: ITipoConvenio[] = [{ id: 456 }];
          expectedResult = service.addTipoConvenioToCollectionIfMissing(tipoConvenioCollection, tipoConvenio);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tipoConvenio);
        });

        it('should add only unique TipoConvenio to an array', () => {
          const tipoConvenioArray: ITipoConvenio[] = [{ id: 123 }, { id: 456 }, { id: 10063 }];
          const tipoConvenioCollection: ITipoConvenio[] = [{ id: 123 }];
          expectedResult = service.addTipoConvenioToCollectionIfMissing(tipoConvenioCollection, ...tipoConvenioArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const tipoConvenio: ITipoConvenio = { id: 123 };
          const tipoConvenio2: ITipoConvenio = { id: 456 };
          expectedResult = service.addTipoConvenioToCollectionIfMissing([], tipoConvenio, tipoConvenio2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tipoConvenio);
          expect(expectedResult).toContain(tipoConvenio2);
        });

        it('should accept null and undefined values', () => {
          const tipoConvenio: ITipoConvenio = { id: 123 };
          expectedResult = service.addTipoConvenioToCollectionIfMissing([], null, tipoConvenio, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tipoConvenio);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
