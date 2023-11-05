import { Button, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField } from '@mui/material'
import React, { useEffect, useState } from 'react'
import { TIME_OUT, debouncing } from '../../utils/Deboucing';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom'

import { DATA_INVENTORY } from '../../redux/selectors/selectors';
import FileUploadIcon from '@mui/icons-material/FileUpload';
import { GET_ALL_INVENTORY_BY_STORAGE_ID } from '../../redux/api/service/inventoryService';
import LoadingComponent from '../../components/loading/LoadingComponent';
import { exportExcel } from '../../utils/ExportExcel';

function InventoryDetailAdmin() {

  const [loading, setLoading] = useState(true);

  const { id, storageName } = useParams();

  const inventory = useSelector(DATA_INVENTORY);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleExportExcelInventory = async (id) => {
    await exportExcel(`/api/v1/inventory/export/${id}`, `inventory_${id}`);
  }

  const [search, setSearch] = useState('');
  const handleChangeSearch = (e) => setSearch(e.target.value);

  useEffect(() => {
    dispatch(GET_ALL_INVENTORY_BY_STORAGE_ID({ search: search, id: id }));
    if (inventory) {
      setTimeout(() => {
        setLoading(false);
      }, 800)
    }
  }, [search])

  return (
    <div>
      <div className='flex justify-center text-3xl font-semibold uppercase py-5'>quản lý tồn kho trong kho : {storageName}</div>
      <div className='flex flex-col gap-5 '>
        <div className='flex justify-between'>
          <div className="actions flex gap-3">
            <Button onClick={() => navigate('/admin/reports/inventory')} variant="contained" className='flex gap-2'>Quay Lại</Button>
          </div>
          <div className="add_manager"></div>
        </div>
        <div className="header bg-white p-6 shadow-md">
          <TextField onChange={debouncing(handleChangeSearch, TIME_OUT)} id="outlined-basic" size='small' fullWidth label="Tìm kiếm theo báo cáo" name='search' variant="outlined" />
        </div>
      </div>
      <div className="content w-full mt-5">
        <TableContainer component={Paper} >
          <Table sx={{ width: '100%' }} aria-label="simple table">
            <TableHead>
              <TableRow>
                <TableCell align="center">STT</TableCell>
                <TableCell align="center">NGÀY TẠO</TableCell>
                <TableCell align='center'>BÁO CÁO</TableCell>
                <TableCell align='center'>HÀNH ĐỘNG</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {!loading && inventory && inventory.map((item, index) => {
                return (
                  <TableRow
                    key={item?.id}
                    sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                    className='hover:bg-slate-200 transition-all'
                  >
                    <TableCell onClick={() => navigate(`/admin/reports/inventory/detail/list/${item.id}/in/${id}/${storageName}`)} align='center'>{index + 1}</TableCell>
                    <TableCell onClick={() => navigate(`/admin/reports/inventory/detail/list/${item.id}/in/${id}/${storageName}`)} align='center'>{item?.created} </TableCell>
                    <TableCell onClick={() => navigate(`/admin/reports/inventory/detail/list/${item.id}/in/${id}/${storageName}`)} align='center'>{item?.note}</TableCell>
                    <TableCell align='center'><Button variant="outlined" onClick={() => handleExportExcelInventory(item.id)} className='flex gap-2'> <FileUploadIcon /> <span>Xuất File Excel</span> </Button></TableCell>
                  </TableRow>
                )
              })}
            </TableBody>
          </Table>
        </TableContainer>
        {loading && <LoadingComponent />}
      </div>
    </div>
  )
}

export default InventoryDetailAdmin