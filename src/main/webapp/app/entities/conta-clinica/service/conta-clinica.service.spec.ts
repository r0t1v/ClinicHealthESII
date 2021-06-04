import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IContaClinica, ContaClinica } from '../conta-clinica.model';

import { ContaClinicaService } from './conta-clinica.service';

describe('Service Tests', () => {
  describe('ContaClinica Service', () => {
    let service: ContaClinicaService;
    let httpMock: HttpTestingController;
    let elemDefault: IContaClinica;
    let expectedResult: IContaClinica | IContaClinica[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ContaClinicaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        cpf: 'AAAAAAA',
        senha: 'AAAAAAA',
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

      it('should create a ContaClinica', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ContaClinica()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ContaClinica', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            cpf: 'BBBBBB',
            senha: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ContaClinica', () => {
        const patchObject = Object.assign({}, new ContaClinica());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ContaClinica', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            cpf: 'BBBBBB',
            senha: 'BBBBBB',
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

      it('should delete a ContaClinica', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addContaClinicaToCollectionIfMissing', () => {
        it('should add a ContaClinica to an empty array', () => {
          const contaClinica: IContaClinica = { id: 123 };
          expectedResult = service.addContaClinicaToCollectionIfMissing([], contaClinica);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contaClinica);
        });

        it('should not add a ContaClinica to an array that contains it', () => {
          const contaClinica: IContaClinica = { id: 123 };
          const contaClinicaCollection: IContaClinica[] = [
            {
              ...contaClinica,
            },
            { id: 456 },
          ];
          expectedResult = service.addContaClinicaToCollectionIfMissing(contaClinicaCollection, contaClinica);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ContaClinica to an array that doesn't contain it", () => {
          const contaClinica: IContaClinica = { id: 123 };
          const contaClinicaCollection: IContaClinica[] = [{ id: 456 }];
          expectedResult = service.addContaClinicaToCollectionIfMissing(contaClinicaCollection, contaClinica);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contaClinica);
        });

        it('should add only unique ContaClinica to an array', () => {
          const contaClinicaArray: IContaClinica[] = [{ id: 123 }, { id: 456 }, { id: 81198 }];
          const contaClinicaCollection: IContaClinica[] = [{ id: 123 }];
          expectedResult = service.addContaClinicaToCollectionIfMissing(contaClinicaCollection, ...contaClinicaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const contaClinica: IContaClinica = { id: 123 };
          const contaClinica2: IContaClinica = { id: 456 };
          expectedResult = service.addContaClinicaToCollectionIfMissing([], contaClinica, contaClinica2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contaClinica);
          expect(expectedResult).toContain(contaClinica2);
        });

        it('should accept null and undefined values', () => {
          const contaClinica: IContaClinica = { id: 123 };
          expectedResult = service.addContaClinicaToCollectionIfMissing([], null, contaClinica, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contaClinica);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
