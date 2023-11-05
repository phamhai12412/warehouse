import { Button, TableCell, TableRow, TextField, Tooltip } from '@mui/material'
import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux';

import { ADD_INVENTORY } from '../../redux/api/service/inventoryService';
import CheckBoxIcon from '@mui/icons-material/CheckBox';
import CloseIcon from '@mui/icons-material/Close';
import { DATA_PRODUCT } from '../../redux/selectors/selectors';
import DownloadIcon from '@mui/icons-material/Download';
import { GET_ALL_PRODUCT_BY_STORAGE_ID } from '../../redux/api/service/productService';
import QuestionMarkIcon from '@mui/icons-material/QuestionMark';
import UploadIcon from '@mui/icons-material/Upload';
import { downloadForm } from '../../utils/FileInventory';
import { useNavigate } from 'react-router-dom';
import { validateBlank } from '../../utils/ValidationForm';
import { validateInvetoryAndProduct } from '../../utils/ValidateProduct';

function FormInventory({ handleCloseForm, handleReload }) {

  const dispatch = useDispatch();
  const navigate = useNavigate();

  const [user, setUser] = useState(JSON.parse(localStorage.getItem('user')));

  const [note, setNote] = useState('');

  const products = useSelector(DATA_PRODUCT);

  const [example, setExample] = useState(false);

  const handleDownloadExample = async () => {
    await downloadForm(`/api/v1/inventory/export-report-form/${user.storageId}`, "import_inventory");
    setExample(true);
  }

  const [file, setFile] = useState(null);
  const [upload, setUpload] = useState(false);
  const handleUploadFileExcel = (e) => {
    setFile(e.target.files[0])

    setUpload(true);
  }

  const [errorBlank, setErrorBlank] = useState('');

  const handleConfirm = async () => {

    // validate blank
    if (validateBlank(note)) {
      setErrorBlank('Không Được Để Trống');
      return;
    }

    const formData = new FormData();
    formData.append('file', file);
    formData.append('note', note);
    dispatch(ADD_INVENTORY({ formData: formData, id: user.storageId }));

    setErrorBlank("");
    handleCloseForm();
    setTimeout(() => {
      handleReload();
    }, 100)
  }

  const getFormatDateNow = () => {
    let now = new Date();
    return now.getDate() + " - " + (now.getMonth() + 1) + " - " + now.getFullYear();
  }

  useEffect(() => {
    setErrorBlank("");
    if (user) {
      dispatch(GET_ALL_PRODUCT_BY_STORAGE_ID({ search: "", filter: 'ALL', id: user.storageId }));
    } else {
      navigate('/');
    }
  }, [])

  return (
    <TableRow>
      <TableCell align='center'><CloseIcon onClick={handleCloseForm} /></TableCell>
      <TableCell align='center'><p className='tracking-wide'>{getFormatDateNow()}</p></TableCell>
      <TableCell align='center'>
        <div className='relative'>
          <TextField
            error={errorBlank}
            fullWidth size='small'
            id="outlined-basic"
            label={errorBlank ? errorBlank : "Note"}
            variant="outlined"
            value={note}
            onChange={(e) => setNote(e.target.value)}
          />
          <Tooltip title="Tiêu đề báo cáo tồn kho"><QuestionMarkIcon className='absolute right-2 top-1/2 -translate-y-1/2' /></Tooltip>
        </div>
      </TableCell>
      <TableCell align='center'>
        <div className='flex gap-3 justify-center'>
          {example ?
            (upload ? <>
              <input type="file" name="file" id="file1" style={{ display: 'none' }} onChange={handleUploadFileExcel} />
              <Button variant="contained" ><label htmlFor="file1" className='uppercase'><UploadIcon /> Nhập File Excel</label></Button>
              <Button variant="contained" onClick={handleConfirm} color='success' className='uppercase'><CheckBoxIcon /> Xác Nhận</Button>
            </> :
              <>
                <input type="file" name="file" id="file2" style={{ display: 'none' }} onChange={handleUploadFileExcel} />
                <Button variant="contained" ><label htmlFor="file2" className='uppercase'><UploadIcon /> Nhập File Excel</label></Button>
              </>
            ) : (
              validateInvetoryAndProduct(products) ?
                <p className='uppercase text-red-500'>Bạn Không còn sản phẩm nào</p> :
                <Button variant="outlined" onClick={handleDownloadExample} className='uppercase'><DownloadIcon />Xuất File Excel Mẫu</Button>
            )
          }
        </div>
      </TableCell>
    </TableRow>
  )
}

export default FormInventory