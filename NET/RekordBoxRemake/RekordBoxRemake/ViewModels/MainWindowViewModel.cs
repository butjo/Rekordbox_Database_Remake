using Prism.Commands;
using Prism.Mvvm;
using System.Reflection;
using System.Windows.Forms;

namespace RekordBoxRemake.ViewModels
{
    public class MainWindowViewModel : BindableBase
    {
        private string _title = "RekordBox Remake V"+ Assembly.GetEntryAssembly().GetName().Version;
        public string Title
        {
            get { return _title; }
            set { SetProperty(ref _title, value); }
        }
        private bool startAllowed;
        public bool StartAllowed
        {
            get { return startAllowed; }
            set { SetProperty(ref startAllowed, value); }
        }

        private string xmlPath;
        public string XmlPath
        {
            get { return xmlPath; }
            set { SetProperty(ref xmlPath, value); }
        }
        public MainWindowViewModel()
        {
            startAllowed = false;
        }

        private DelegateCommand _selectXmlCommand;
        public DelegateCommand SelectXmlCommand =>
            _selectXmlCommand ?? (_selectXmlCommand = new DelegateCommand(ExecuteSelectXmlCommand));

        void ExecuteSelectXmlCommand()
        {
            using (OpenFileDialog openFileDialog = new OpenFileDialog())
            {
                openFileDialog.InitialDirectory = "c:\\";
                openFileDialog.Filter = "xml files (*.xml)|*.xml|All files (*.*)|*.*";
                openFileDialog.FilterIndex = 2;
                openFileDialog.RestoreDirectory = true;

                if (openFileDialog.ShowDialog() == DialogResult.OK)
                {
                    //Get the path of specified file
                    XmlPath = openFileDialog.FileName;
                }
            }
        }
        private string zipPath;
        public string ZipPath
        {
            get { return zipPath; }
            set { SetProperty(ref zipPath, value); }
        }
        private DelegateCommand _selectZipPathCommand;
        public DelegateCommand SelectZipPathCommand =>
            _selectZipPathCommand ?? (_selectZipPathCommand = new DelegateCommand(ExecuteSelectZipPathCommand));

        void ExecuteSelectZipPathCommand()
        {
            using (FolderBrowserDialog folderDialog = new FolderBrowserDialog())
            {
                if (folderDialog.ShowDialog() == DialogResult.OK)
                {
                    //Get the path of specified file
                    ZipPath = folderDialog.SelectedPath;
                }
            }
        }

        private DelegateCommand _startRemakeCommand;
        public DelegateCommand StartRemakeCommand =>
            _startRemakeCommand ?? (_startRemakeCommand = new DelegateCommand(ExecuteStartRemakeCommand));

        void ExecuteStartRemakeCommand()
        {

        }
    }
}
