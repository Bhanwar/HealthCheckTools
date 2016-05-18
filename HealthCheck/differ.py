#!/usr/bin/env python
import os
import sys
import subprocess
import hashlib
import argparse
import shutil


class Git:
    def __init__(self, branch):
        self.branch = branch

    def __enter__(self):
        subprocess.check_call(["git", "checkout", self.branch])

    def __exit__(self, type, value, traceback):
        if value is not None:
            raise value

class Build:
    def __init__(self, tmp_path, branch_name):
        self.tmp_path = tmp_path
        self.branch_name = branch_name

    def __enter__(self):
        subprocess.check_call(["mvn", "clean", "install", "-U"])

    def __exit__(self, type, value, traceback):
        if value is not None:
            raise value

        path = "%s/%s" % (self.tmp_path, self.branch_name)
        subprocess.check_call(["rm", "-rf", path])
        os.mkdir(path)
        cmd = 'mv target/*.jar %s && cd %s && jar xf *.jar && rm -f *.jar' % (path, path)
        os.system(cmd)

class Diff:
    def __init__(self, tmp_path, src_branch, target_branch, print_diff):
        self.tmp_path = tmp_path
        self.src_branch = src_branch
        self.target_branch = target_branch
        self.src_path = "%s/%s" % (tmp_path, src_branch)
        self.target_path = "%s/%s" % (tmp_path, target_branch)
        self.print_diff = print_diff

    def __enter__(self):
        pass

    def __exit__(self, type, value, traceback):
        if value is not None:
            raise value
        src_result = self.md5(self.src_path)
        tgt_result = self.md5(self.target_path)

        src_keys = src_result.keys()
        src_keys.sort()
        tgt_keys = tgt_result.keys()
        tgt_keys.sort()

        if src_keys != tgt_keys:
            raise
        else:
            for key in src_keys:
                if src_result[key] != tgt_result[key]:
                    print "\n\n\n\n"
                    print ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
                    print "different hash for %s" % key
                    if self.print_diff:
                        subprocess.call(["diff", "%s%s" % (self.src_path, key), "%s%s" % (self.target_path, key)])
                    print "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
        print "COMPLETED!!!"


    def md5(self, path):
        result = {}
        for root, dirs, files in os.walk(path):
            for file in files:
                current_path = root + "/" + file
                md5sum = hashlib.md5(open(current_path, 'rb').read()).hexdigest()
                current_path = current_path.split(path)[-1:][0]
                result[current_path] = md5sum
        return result


parser = argparse.ArgumentParser(description='Java differ')
parser.add_argument('--tmp_path', type=str, default="/tmp", help='tmp path where diffing takes palce')
parser.add_argument('--src_branch', type=str, default="master", help='branch where release is building')
parser.add_argument('--target_branch', type=str, help='branch where your code is merged in')
parser.add_argument('--print_diff', type=bool, default=True, help='print diff of different hashed file')
args = parser.parse_args()

with Diff(args.tmp_path, args.src_branch, args.target_branch, args.print_diff):
    for branch in [args.src_branch, args.target_branch]:
        with Git(branch):
            with Build(args.tmp_path, branch):
                pass
